package com.mahdivajdi.modernweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.mahdivajdi.modernweather.databinding.FragmentCitiesBinding
import com.mahdivajdi.modernweather.ui.adapter.CitiesFragmentAdapter
import com.mahdivajdi.modernweather.ui.viewmodel.CitiesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null
    val binding: FragmentCitiesBinding get() = _binding!!

    val viewModel by activityViewModels<CitiesViewModel>()

    /*private val viewModel: CitiesViewModel by activityViewModels {
        CitiesViewModelFactory(
            (activity?.application as App),
            CityRepositoryImpl(
                CityRemoteDataSource(GeocodeApi),
                (activity?.application as App).database.cityDao()
            )
        )
    }*/

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