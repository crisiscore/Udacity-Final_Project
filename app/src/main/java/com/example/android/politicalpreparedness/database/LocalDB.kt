package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Room

object LocalDB {

    private const val DATABASE_NAME = "election_database"
    fun createElectionsDao(context: Context): ElectionDao {
        return Room.databaseBuilder(
            context.applicationContext,
            ElectionDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build().electionDao()
    }

}