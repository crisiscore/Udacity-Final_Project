package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Result
import kotlinx.coroutines.launch

class ElectionsViewModel(val dataSource: ElectionDataSource) : ViewModel() {
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    fun getUpcomingElections() {
        viewModelScope.launch {
            when (val result = dataSource.getAllElections()) {
                is Result.Success -> {
                    _upcomingElections.value = result.data
                }
                is Result.Error -> {
                    _upcomingElections.value = emptyList()
                    Log.e(
                        "ElectionsViewModel",
                        "Error getting upcoming elections ${result.message}"
                    )
                }
            }
        }
    }

    fun getSavedElections() {
        viewModelScope.launch {
            when (val result = dataSource.getAllSavedElections()) {
                is Result.Success -> {
                    _savedElections.value = result.data
                }
                is Result.Error -> {
                    _savedElections.value = emptyList()
                    Log.e("ElectionsViewModel", "Error getting saved elections ${result.message}")
                }
            }
        }
    }
}