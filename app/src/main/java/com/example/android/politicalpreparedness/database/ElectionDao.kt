package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

private const val TABLE_NAME = "election_table"
@Dao
interface ElectionDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllElections(): List<Election>

    @Query("SELECT * FROM $TABLE_NAME WHERE isElectionSaved = 1")
    fun getSavedElections(): List<Election>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun getElectionById(id: Int): Election?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllElections(elections: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearElections()

}