package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.mapResponseToRepresentativeList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RepresentativeRepository (
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RepresentativeDataSource {

    override suspend fun getRepresentativeList(address: String): Result<List<Representative>> = withContext(ioDispatcher) {
        try {
            val representativesResponse = civicsApiService.getRepresentatives(address)
            if (representativesResponse != null){
                val representatives = representativesResponse.mapResponseToRepresentativeList()
                return@withContext Result.Success(representatives)
            }
            return@withContext Result.Error("Could not get representative list!")
        }
        catch (e: Exception){
            if (e is HttpException) {
                return@withContext Result.Error(e.response().toString())
            }
            return@withContext Result.Error(e.localizedMessage)
        }
    }

}