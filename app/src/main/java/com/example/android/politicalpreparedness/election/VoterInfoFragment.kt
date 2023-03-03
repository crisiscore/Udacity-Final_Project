package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private val voterInfoViewModel: VoterInfoViewModel by viewModel()
    private val argument: VoterInfoFragmentArgs? by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = voterInfoViewModel
        argument?.election?.let { voterInfoViewModel.initElection(it) }
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        voterInfoViewModel.webUrl.observe(viewLifecycleOwner){ webUrl ->
            webUrl.goToUrl()
        }
    }

    private fun String.goToUrl() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
        startActivity(browserIntent)
    }

}