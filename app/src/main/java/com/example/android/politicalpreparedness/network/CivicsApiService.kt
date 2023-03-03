package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_ELECTIONS = "elections"
private const val API_VOTER_INFO = "voterinfo"
private const val API_REPRESENTATIVES = "representatives"

interface CivicsApiService {
    @GET(API_ELECTIONS)
    suspend fun getElections(): ElectionResponse

    @GET(API_VOTER_INFO)
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
    ): VoterInfoResponse?

    @GET(API_REPRESENTATIVES)
    suspend fun getRepresentatives(
        @Query("address") address: String
    ): RepresentativeResponse?

}