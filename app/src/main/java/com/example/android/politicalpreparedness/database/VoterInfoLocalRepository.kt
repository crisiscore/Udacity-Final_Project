package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.concatStateAndCountry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class VoterInfoLocalRepository (
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): VoterInfoDataSource {

    override suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse> = withContext(ioDispatcher) {

        val electionId = election.id

        val address = concatStateAndCountry(election.division.state, election.division.country)

        try {
            val voterInfoResponse = civicsApiService.getVoterInfo(electionId = electionId, address = address)
            if (voterInfoResponse != null){
                return@withContext Result.Success(voterInfoResponse)
            }
            return@withContext Result.Error("Cannot get voter info!")
        }
        catch (e: Exception){
            if (e is HttpException) {
                return@withContext Result.Error(e.response().toString())
            }
            return@withContext Result.Error(e.localizedMessage)
        }
    }
}
