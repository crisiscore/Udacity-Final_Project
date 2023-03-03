package com.example.android.politicalpreparedness.database

import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Result
import com.example.android.politicalpreparedness.utils.EspressoIdlingResource.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ElectionLocalRepository(
    private val electionDao: ElectionDao,
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    companion object {
        private val TAG = ElectionLocalRepository::class.java.simpleName
    }

    override suspend fun getAllElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val electionsResponse = civicsApiService.getElections().elections
            if (electionsResponse.isNotEmpty())
                return@withContext Result.Success(electionsResponse)
            else
                return@withContext Result.Error("Could not get elections!")
        } catch (e: Exception) {
            if (e is HttpException) {
                return@withContext Result.Error(e.response().toString())
            } else {
                return@withContext Result.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> = wrapEspressoIdlingResource {
        withContext(ioDispatcher) {
            try {
                val election = electionDao.getElectionById(id)
                if (election != null) {
                    return@withContext Result.Success(election)
                } else {
                    return@withContext Result.Error("Election is not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun insertAllElections(elections: List<Election>) =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                electionDao.insertAllElections(elections)
            }
        }

    override suspend fun insertElection(election: Election) = wrapEspressoIdlingResource {
        withContext(ioDispatcher) {
            electionDao.insertElection(election)
        }
    }

    override suspend fun getAllSavedElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val electionsResponse = electionDao.getSavedElections()
            return@withContext Result.Success(electionsResponse)
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun deleteElection(election: Election) = withContext(ioDispatcher) {
        electionDao.deleteElection(election)
    }
}
