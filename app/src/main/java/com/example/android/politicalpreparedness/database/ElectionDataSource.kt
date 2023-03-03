package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.network.models.Election

interface ElectionDataSource {
    suspend fun getAllElections(): Result<List<Election>>
    suspend fun getElectionById(id: Int): Result<Election>
    suspend fun insertAllElections(elections: List<Election>)
    suspend fun insertElection(election: Election)
    suspend fun getAllSavedElections(): Result<List<Election>>
    suspend fun deleteElection(election: Election)
}