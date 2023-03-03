package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding
    private lateinit var networkElectionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = electionsViewModel

        initRecyclerview()
        initViewModel()
        observeViewModel()

        return binding.root

    }

    private fun observeViewModel() {
        electionsViewModel.apply {
            upcomingElections.observe(viewLifecycleOwner) {
                networkElectionsAdapter.submitList(it)
            }
            savedElections.observe(viewLifecycleOwner) {
                savedElectionsAdapter.submitList(it)
            }
        }
    }

    private fun initViewModel() {
        electionsViewModel.apply {
            getUpcomingElections()
            getSavedElections()
        }
    }

    private fun initRecyclerview() {
        val electionListener = ElectionListener {
            navigateToVoterInfo(it)
        }
        networkElectionsAdapter = ElectionListAdapter(electionListener)
        savedElectionsAdapter = ElectionListAdapter(electionListener)

        with(binding) {
            rvUpcomingElections.adapter = networkElectionsAdapter
            rvSavedElections.adapter = savedElectionsAdapter
        }
    }

    private fun navigateToVoterInfo(election: Election) {
        val navigationDirection =
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
        this.findNavController().navigate(navigationDirection)
    }

}