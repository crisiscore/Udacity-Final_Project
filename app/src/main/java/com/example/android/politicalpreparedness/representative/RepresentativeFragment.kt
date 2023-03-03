package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.os.Looper
import android.view.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.geoCodeLocation
import com.example.android.politicalpreparedness.utils.hideKeyboard
import com.example.android.politicalpreparedness.utils.isPermissionGranted
import com.example.android.politicalpreparedness.utils.showSnackBar
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    companion object {
        private const val LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
        const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val MOTION_LAYOUT_STATE = "motionLayoutState"
    }

    private val representativeViewModel: RepresentativeViewModel by viewModel()
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var representativeListAdapter: RepresentativeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = representativeViewModel

        savedInstanceState?.getInt(MOTION_LAYOUT_STATE)?.let {
            binding.motionLayout.transitionToState(it)
        }

        initRecyclerview()
        clickActions()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        representativeViewModel.apply {
            representativeList.observe(viewLifecycleOwner) {
                representativeListAdapter.submitList(it)
            }
        }
    }

    private fun clickActions() {
        binding.apply {
            btnSearch.setOnClickListener {
                hideKeyboard(view = requireView(), activity = requireActivity())
                val address = Address(
                    line1 = etAddressLine1.text.toString(),
                    line2 = etAddressLine2.text.toString(),
                    city = etCity.text.toString(),
                    state = spnState.selectedItem.toString(),
                    zip = etZip.text.toString()
                )
                representativeViewModel.findRepresentative(address)
            }
            btnLocation.setOnClickListener {
                hideKeyboard(view = requireView(), activity = requireActivity())
                checkLocationPermissions()
            }
        }

    }

    private fun initRecyclerview() {
        representativeListAdapter = RepresentativeListAdapter()

        with(binding) {
            rvRepresentatives.adapter = representativeListAdapter
        }
    }

    private fun checkLocationPermissions() {
        if (requireContext().isPermissionGranted()) {
            checkDeviceLocationSettings()
        } else {
            requestPermissionLauncher.launch(PERMISSION)
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkDeviceLocationSettings()
        } else {
            showSnackBar(message = R.string.permission_required, view = requireView())
        }
    }

    private val locationSettingLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK)
            getCurrentLocation()
        else {
            showSnackBar(message = R.string.permission_required, view = requireView())
        }
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun getCurrentLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.let {
                    val address = geoCodeLocation(it.lastLocation, context = requireContext())
                    if (address != null) {
                        representativeViewModel.setSearchAddress(address)
                    }
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }
        val locationRequest = LocationRequest.create().apply {
            priority = LOCATION_REQUEST_PRIORITY
            interval = 0
            fastestInterval = 0

        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }

    @SuppressLint("VisibleForTests")
    private fun checkDeviceLocationSettings(isResolved: Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LOCATION_REQUEST_PRIORITY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && isResolved) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            } else {
                showSnackBar(message = R.string.permission_required, view = requireView())
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                getCurrentLocation()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
         outState.putInt(MOTION_LAYOUT_STATE, binding.motionLayout.currentState)
    }


}