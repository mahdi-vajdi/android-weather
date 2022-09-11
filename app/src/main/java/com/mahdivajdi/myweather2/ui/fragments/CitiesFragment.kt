package com.mahdivajdi.myweather2.ui.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.mahdivajdi.myweather2.App
import com.mahdivajdi.myweather2.data.remote.CityRemoteDataSource
import com.mahdivajdi.myweather2.data.remote.GeocodeApi
import com.mahdivajdi.myweather2.data.remote.OneCallApi
import com.mahdivajdi.myweather2.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.myweather2.data.repository.CityRepository
import com.mahdivajdi.myweather2.data.repository.WeatherRepository
import com.mahdivajdi.myweather2.databinding.FragmentCitiesBinding
import com.mahdivajdi.myweather2.ui.adapter.CitiesFragmentAdapter
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModel
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModelFactory
import kotlinx.coroutines.launch


class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null
    val binding: FragmentCitiesBinding get() = _binding!!

    private val viewModel: CitiesViewModel by activityViewModels {
        CitiesViewModelFactory(
            (activity?.application as App),
            CityRepository(
                CityRemoteDataSource(GeocodeApi),
                (activity?.application as App).database.cityDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButtonCity.setOnClickListener {
            val action = CitiesFragmentDirections.actionCitiesFragmentToAddCityFragment()
            view.findNavController().navigate(action)
        }

        val adapter = CitiesFragmentAdapter { city ->
            viewModel.deleteCity(city)
        }
        binding.recyclerView.adapter = adapter
        viewModel.localCityList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}