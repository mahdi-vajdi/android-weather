package com.mahdivajdi.myweather2.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahdivajdi.myweather2.App
import com.mahdivajdi.myweather2.data.remote.CityRemoteDataSource
import com.mahdivajdi.myweather2.data.remote.GeocodeApi
import com.mahdivajdi.myweather2.data.repository.CityRepository
import com.mahdivajdi.myweather2.databinding.FragmentAddCityBinding
import com.mahdivajdi.myweather2.ui.adapter.AddCityFragmentAdapter
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModel
import com.mahdivajdi.myweather2.ui.viewmodel.CitiesViewModelFactory


class AddCityFragment : Fragment() {

    companion object {
        private const val TAG = "CitiesData"
    }

    private var _binding: FragmentAddCityBinding? = null
    private val binding: FragmentAddCityBinding get() = _binding!!

    private val viewModel: CitiesViewModel by activityViewModels {
        CitiesViewModelFactory(
            activity!!.application,
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
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)

        // Request focus for searchView so the keyboard opens automatically
        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            if (hasFocus) {
                showInputMethod(searchView.findFocus())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        // Listen to the search queries
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                text?.let {
                    viewModel.getRemoteCityByName(text)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                }
                return false
            }
        })

        val adapter = AddCityFragmentAdapter { clickedCity ->
            viewModel.insertNewCity(clickedCity)
            activity?.onBackPressed()
        }
        binding.recyclerViewCities.adapter = adapter
        viewModel.remoteCityList.observe(this.viewLifecycleOwner) { cityList ->
            cityList.let {
                adapter.submitList(it)
                Log.i(TAG, "onViewCreated: $it")
            }
        }
        binding.recyclerViewCities.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showInputMethod(view: View) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}