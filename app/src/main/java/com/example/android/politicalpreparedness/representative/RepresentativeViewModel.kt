package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.RepresentativeDataSource
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    val app: Application,
    val representativeDataSource: RepresentativeDataSource,
    val handle: SavedStateHandle
) :
    ViewModel() {

    companion object {
        private val TAG = RepresentativeViewModel::class.java.simpleName
        private const val STATE_REPRESENTATIVES = "representatives"
    }

    private val _representativeList = handle.getLiveData<List<Representative>>(STATE_REPRESENTATIVES , emptyList())
    val representativeList: LiveData<List<Representative>>
        get() = _representativeList

    val address = MutableLiveData<Address>()

    init {
        address.value = Address()
        _representativeList.value = handle.get<List<Representative>>(STATE_REPRESENTATIVES)
    }

    fun setSearchAddress(address: Address) {
        this.address.value = address
    }

    fun findRepresentative(address: Address) {
        loadRepresentatives(address)
    }

    private fun loadRepresentatives(address: Address) {
        viewModelScope.launch {
            val addressStr = address.toFormattedString()
            when (val result = representativeDataSource.getRepresentativeList(addressStr)) {
                is Result.Success<List<Representative>> -> {
                    _representativeList.value = result.data
                    handle[STATE_REPRESENTATIVES] = result.data
                }
                is Result.Error -> {
                    _representativeList.value = emptyList()
                    Log.e(TAG, "Load Representative Error: " + result.message)
                }
            }
        }
    }

}
