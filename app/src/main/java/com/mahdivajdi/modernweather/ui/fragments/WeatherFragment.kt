package com.mahdivajdi.modernweather.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahdivajdi.modernweather.databinding.FragmentWeatherBinding
import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.mahdivajdi.modernweather.ui.adapter.HourlyForecastListAdapter
import com.mahdivajdi.modernweather.ui.getTemp
import com.mahdivajdi.modernweather.ui.setIcon
import com.mahdivajdi.modernweather.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val CITY_ARG = "city"

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private var city: CityDomainModel? = null

    // Preferences like temp unit
    private var tempUnit: String = ""
    private lateinit var prefs: SharedPreferences
    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        if (key.equals("temp_unit")) {
            tempUnit = prefs.getString("temp_unit", "") ?: ""
            initViews()
            Log.d(TAG, "pref changed: $tempUnit")
        }
    }

    private val viewModel by activityViewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { argument ->
            city = argument.getParcelable(CITY_ARG)
            city?.let {
                viewModel.init(it)
            }
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        tempUnit = prefs.getString("temp_unit", "") ?: ""
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate the XML binding
        binding.viewModel = viewModel
        city?.let { binding.city = it }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        // Set layout manager for hourly forecast list
        binding.recyclerViewWeatherHourly.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        // Populate the views containing temperature and units
        initViews()

        // Click event listeners
        city?.let { city ->
            binding.buttonWeatherToDailyForecast.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToDailyForecastFragment(city)
                view.findNavController().navigate(action)
            }
        }
    }

    private fun initViews() {
        binding.textViewWeatherTempUnit.text = when (getTempUnit()) {
            "metric" -> "℃"
            "imperial" -> "℉"
            else -> ""
        }
        viewModel.currentWeather.observe(viewLifecycleOwner) {
            binding.textViewWeatherCurrent.text = getTemp(it.temp, tempUnit).toString()
        }
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyList ->
            binding.recyclerViewWeatherHourly.adapter =
                HourlyForecastListAdapter(hourlyList, tempUnit)
        }
        viewModel.dailyForecast.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.imageViewWeatherTodayIcon.setIcon(it[0].icon)
                binding.textViewWeatherTodayTemp.text =
                    "${getTemp(it[0].minTemp, tempUnit)}° / ${getTemp(it[0].maxTemp, tempUnit)}°"

                binding.imageViewWeatherTomorrowIcon.setIcon(it[1].icon)
                binding.textViewWeatherTodayTemp.text =
                    "${getTemp(it[1].minTemp, tempUnit)} / ${getTemp(it[1].maxTemp, tempUnit)}"
            }
        }
    }

    private fun getTempUnit() = prefs.getString("temp_unit", "no temp unit") ?: "no temp unit"

    override fun onStop() {
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "temp_unit"

        @JvmStatic
        fun newInstance(city: CityDomainModel) = WeatherFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CITY_ARG, city)
            }
        }
    }
}

