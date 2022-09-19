package com.mahdivajdi.modernweather.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mahdivajdi.modernweather.R
import com.mahdivajdi.modernweather.databinding.FragmentMainBinding
import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.mahdivajdi.modernweather.ui.viewmodel.CitiesViewModel
import com.mahdivajdi.modernweather.workers.RefreshAllWeatherWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    val viewModel by activityViewModels<CitiesViewModel>()

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocationData()
        } else {
            Log.d("locationy", "Location permission denied")
        }
    }

    private lateinit var workManager: WorkManager
    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocationData()

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
        viewModel.currentCity.observe(viewLifecycleOwner) {

            Log.d("locationy", "MainFragment: currentCity: $it")
            viewModel.insertNewCity(it)
        }

        // start the view pager fragments
        viewModel.localCityList.observe(viewLifecycleOwner) { cityList ->
            val viewPager = binding.viewPagerMain
            val viewPagerAdapter =
                WeatherViewPagerAdapter(requireActivity().supportFragmentManager,
                    lifecycle,
                    cityList)
            viewPager.adapter = viewPagerAdapter
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

    private fun getCurrentLocationData() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        fusedLocationProvider.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("locationy", "getCurrentLocation: location is: lat=$lat  lon=$lon")
                    viewModel.getRemoteCityByCoordinates(lat, lon,1)
                } else {
                    Log.d("locationy", "getCurrentLocation: location is null")
                }
            }
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
