package com.mahdivajdi.myweather2.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log.d
import android.util.Log.i
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.mahdivajdi.myweather2.App
import com.mahdivajdi.myweather2.R
import com.mahdivajdi.myweather2.data.remote.CityRemoteDataSource
import com.mahdivajdi.myweather2.data.remote.GeocodeApi
import com.mahdivajdi.myweather2.data.repository.CityRepository
import com.mahdivajdi.myweather2.databinding.FragmentMainBinding
import com.mahdivajdi.myweather2.domain.CityDomainModel
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModel
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModelFactory
import com.mahdivajdi.myweather2.workers.RefreshAllWeatherWorker

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val citiesViewModel: CitiesViewModel by activityViewModels {
        CitiesViewModelFactory(
            requireActivity().application,
            CityRepository(
                CityRemoteDataSource(GeocodeApi),
                (activity?.application as App).database.cityDao()
            )
        )
    }

    private lateinit var workManager: WorkManager
    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(requireActivity().application)
        outputWorkInfo = workManager.getWorkInfosByTagLiveData(WORKER_REFRESH_TAG)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        // Add menu to the fragment
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.cities_fragment_menu_item -> {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToCitiesFragment())
                        true
                    }
                    R.id.settings_fragment_menu_item -> {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Listen to the city data for current location
        citiesViewModel.currentCity.observe(viewLifecycleOwner) {
            citiesViewModel.insertNewCity(it)
        }

        // start the view pager fragments
        citiesViewModel.localCityList.observe(viewLifecycleOwner) { cityList ->
            cityList?.let {
                val viewPager = binding.viewPagerMain
                val viewPagerAdapter =
                    WeatherViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, it)
                viewPager.adapter = viewPagerAdapter
            }
        }

        // Set swipe refresh layout listener
        binding.swipeRefreshMain.setOnRefreshListener {
            val workRequest = OneTimeWorkRequestBuilder<RefreshAllWeatherWorker>()
                .addTag(WORKER_REFRESH_TAG)
                .build()
            workManager.enqueue(workRequest)
            outputWorkInfo.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                val workInfo = it[0]
                if (workInfo.state.isFinished) {
                    binding.swipeRefreshMain.isRefreshing = false
                }
            }
        }
        binding.swipeRefreshMain.setColorSchemeResources(R.color.purple_200)
//        binding.swipeRefreshMain.setProgressBackgroundColorSchemeResource(R.color.teal_200)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "weatherApi"
        private const val WORKER_REFRESH_TAG = "weatherApi"
        private val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}

class WeatherViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val cityList: List<CityDomainModel>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = cityList.size

    override fun createFragment(position: Int) = WeatherFragment.newInstance(cityList[position])


}
