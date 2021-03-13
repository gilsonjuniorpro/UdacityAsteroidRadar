package com.udacity.asteroidradar.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.DetailListener
import com.udacity.asteroidradar.adapter.DetailsAdapter
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.dto.InfoDto
import com.udacity.asteroidradar.repository.NeoRepository
import com.udacity.asteroidradar.viewmodel.DetailViewModel
import com.udacity.asteroidradar.viewmodel.DetailViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var adapter: DetailsAdapter
    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailViewModelFactory: DetailViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        detailViewModelFactory = DetailViewModelFactory(NeoRepository(requireContext()))
        detailViewModel = ViewModelProvider(this, detailViewModelFactory)
            .get(DetailViewModel::class.java)

        //binding.detailViewModel = detailViewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        binding.recyclerDetails.layoutManager = LinearLayoutManager(requireContext())
        adapter = DetailsAdapter(DetailListener { infoDto ->
            openDetail(infoDto)
        })
        binding.recyclerDetails.adapter = adapter

        detailViewModel.list.observe(requireActivity(), Observer { list ->
            adapter.submitList(list)
        })
        detailViewModel.extractList(args)

        if (args.isPotentiallyHazardous != null) {
            if(args.isPotentiallyHazardous){
                binding.ivPicture.setImageResource(R.drawable.ic_meteor_danger)
                binding.tvState.text = getString(R.string.hazardous)
                binding.tvState.setTextColor(Color.RED)
            }else{
                binding.ivPicture.setImageResource(R.drawable.ic_meteor_not_danger)
                binding.tvState.text = getString(R.string.not_hazardous)
                binding.tvState.setTextColor(Color.GREEN)
            }

            args.codename?.let {name ->
                binding.tvTitle.text = name
            }
        }

        binding.ivBack.setOnClickListener{
            navigateToList()
        }

        binding.ivPicture.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }
    }

    private fun openDetail(infoDto: InfoDto){
        displayAstronomicalUnitExplanationDialog()
    }

    private fun navigateToList() {
        findNavController().navigate(
            DetailFragmentDirections.actionDetailFragmentToMainFragment()
        )
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}