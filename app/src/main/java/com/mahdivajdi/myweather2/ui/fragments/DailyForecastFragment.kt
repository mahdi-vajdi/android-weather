package com.mahdivajdi.myweather2.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahdivajdi.myweather2.App
import com.mahdivajdi.myweather2.R
import com.mahdivajdi.myweather2.data.remote.OneCallApi
import com.mahdivajdi.myweather2.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.myweather2.data.repository.WeatherRepository
import com.mahdivajdi.myweather2.databinding.FragmentDailyForecastBinding
import com.mahdivajdi.myweather2.domain.CityDomainModel
import com.mahdivajdi.myweather2.ui.adapter.DailyForecastListAdapter
import com.mahdivajdi.myweather2.ui.viewmodel.WeatherViewModel
import com.mahdivajdi.myweather2.ui.viewmodel.WeatherViewModelFactory

private const val CITY_ARG = "city"

class DailyForecastFragment : Fragment() {

    private var _binding: FragmentDailyForecastBinding? = null
    private val binding get() = _binding!!

    private var city: CityDomainModel? = null

    private val viewModel: WeatherViewModel by activityViewModels {
        WeatherViewModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource(OneCallApi),
                (activity?.application as App).database.currentWeatherDao(),
                (activity?.application as App).database.dailyForecastDao(),
                (activity?.application as App).database.hourlyForecastDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            city = args.getParcelable(CITY_ARG)
            city?.let {
                viewModel.init(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        binding.recyclerViewDaily.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        viewModel.dailyForecast.observe(viewLifecycleOwner) { dailyList ->
            binding.recyclerViewDaily.adapter = DailyForecastListAdapter(dailyList, getTempUnit())
        }
    }

    private fun getTempUnit(): String =
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("temp_unit", "no temp unit") ?: "no temp unit"

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}