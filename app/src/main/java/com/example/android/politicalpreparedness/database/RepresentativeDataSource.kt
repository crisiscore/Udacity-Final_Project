package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.network.models.Result

interface RepresentativeDataSource {
    suspend fun getRepresentativeList(address: String): Result<List<Representative>>
}