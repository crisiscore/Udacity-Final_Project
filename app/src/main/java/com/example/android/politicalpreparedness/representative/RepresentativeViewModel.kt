package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.RepresentativeDataSource
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    val app: Application,
    val representativeDataSource: RepresentativeDataSource
) :
    ViewModel() {

    companion object {
        private val TAG = RepresentativeViewModel::class.java.simpleName
    }

    private val _representativeList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativeList

    val address = MutableLiveData<Address>()
    init {
        address.value = Address()
    }

    fun setSearchAddress(address: Address){
        this.address.value = address
    }

    fun findRepresentative(address: Address){
        loadRepresentatives(address)
    }

    private fun loadRepresentatives(address: Address) {
        viewModelScope.launch {
           val addressStr = address.toFormattedString()
            when (val result = representativeDataSource.getRepresentativeList(addressStr)) {
                is Result.Success<*> -> {
                    _representativeList.value = result.data as List<Representative>
                }
                is Result.Error -> {
                    _representativeList.value = emptyList()
                    Log.e(TAG, "Load Representative Error: " + result.message)
                }
            }
        }
    }

}
