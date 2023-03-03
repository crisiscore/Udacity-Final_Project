package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.DetailFragment.Companion.PERMISSION
import java.util.*

fun Context.isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
    this,
    PERMISSION
) == PackageManager.PERMISSION_GRANTED

fun geoCodeLocation(location: Location, context: Context): Address? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return geocoder.getFromLocation(location.latitude, location.longitude, 1)
        ?.map { address ->
            Address(
                address.thoroughfare,
                address.subThoroughfare,
                address.locality,
                address.adminArea,
                address.postalCode
            )
        }
        ?.first()
}
