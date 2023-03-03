package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.models.Result

interface VoterInfoDataSource {
    suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse>
}