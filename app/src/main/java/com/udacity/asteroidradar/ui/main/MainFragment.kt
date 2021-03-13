package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidsAdapter
import com.udacity.asteroidradar.adapter.NeoListener
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.NeoRepository
import com.udacity.asteroidradar.util.Utils
import com.udacity.asteroidradar.viewmodel.MainViewModel
import com.udacity.asteroidradar.viewmodel.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var neoViewModelFactory: MainViewModelFactory

    companion object {
        const val CORNERS = 25F
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        neoViewModelFactory = MainViewModelFactory(NeoRepository(requireContext()), application)
        viewModel = ViewModelProvider(this, neoViewModelFactory)
            .get(MainViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        val dates = NetworkUtils.getNextSevenDaysFormattedDates()

        viewModel.apod.observe(requireActivity(), Observer { apodWrapper ->
            if (apodWrapper.message == "success") {
                apodWrapper.apod?.apply {
                    if (media_type == "image") {
                        binding.activityMainImageOfTheDay.load(url) {
                            crossfade(true)
                            placeholder(R.drawable.ic_space_sample)
                            transformations(
                                    RoundedCornersTransformation(
                                            CORNERS, CORNERS, CORNERS, CORNERS
                                    )
                            )
                        }
                    }
                    binding.tvApod.text = title
                    binding.activityMainImageOfTheDay.contentDescription = title
                }
            }
        })

        val adapter = AsteroidsAdapter(NeoListener { asteroid ->
            openDetail(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.neo.observe(requireActivity(), { neoWrapper ->
            binding.progressBar.visibility = View.GONE
            if(neoWrapper.message == "success"){
                adapter.submitList(neoWrapper.neo)
            }
        })

        if(Utils.isConnected(requireContext())) {
            viewModel.getApod(null)

            viewModel.getNeoFeed(dates[0], dates[7])
        }else{
            viewModel.getApod()

            viewModel.getAllNeos()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun openDetail(asteroid: Asteroid){
        findNavController().navigate(
            MainFragmentDirections.actionShowDetail(asteroid))
    }
}
