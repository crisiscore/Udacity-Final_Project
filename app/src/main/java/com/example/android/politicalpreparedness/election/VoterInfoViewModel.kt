package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.database.VoterInfoDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(
    private val electionDataSource: ElectionDataSource,
    private val voterInfoDataSource: VoterInfoDataSource
) : ViewModel() {

    companion object {
        private val TAG = VoterInfoResponse::class.java.simpleName
    }

    private val _selectedElection = MutableLiveData<Election>()
    val selectedElection: LiveData<Election>
        get() = _selectedElection

    private val _webUrl = MutableLiveData<String>()
    val webUrl: LiveData<String>
        get() = _webUrl

    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    fun initElection(election: Election) {
        refreshSelectedElection(election)
        getVoterInfo(election)
    }

    fun onFollowElectionButtonClick() {
        _selectedElection.value?.let {
            viewModelScope.launch {
                val updatedElection = updateElectionSavedStatus(it)
                refreshSelectedElection(updatedElection)
            }
        }
    }

    fun updateElectionSavedStatus(election: Election): Election {
        election.isElectionSaved = !election.isElectionSaved
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                electionDataSource.insertElection(election)
            }
        }
        return election
    }

    private fun refreshSelectedElection(election: Election) {
        _selectedElection.value = election
    }

    private fun getVoterInfo(election: Election) = viewModelScope.launch {
        when (val result = voterInfoDataSource.getVoterInfo(election)) {
            is Result.Success<VoterInfoResponse> -> {
                _voterInfoResponse.value = result.data
            }
            else -> {
                Log.e(TAG, "Error")
            }
        }
    }

    fun getVotingLocations() {
        val url = voterInfoResponse.value?.state?.firstOrNull()?.electionAdministrationBody?.votingLocationFinderUrl
        _webUrl.value = url
    }

    fun getBallotInformation() {
        val url = voterInfoResponse.value?.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl
        _webUrl.value = url
    }

}