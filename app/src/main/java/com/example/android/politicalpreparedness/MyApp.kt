package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.*
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(viewModelModule, dataModule))
        }
    }

    private val viewModelModule = module {
        viewModel { ElectionsViewModel(get()) }
        viewModel {
            RepresentativeViewModel(
                app = get(),
                representativeDataSource = get() as RepresentativeDataSource
            )
        }
        viewModel { VoterInfoViewModel(get() as ElectionDataSource, get() as VoterInfoDataSource) }
    }

    private val dataModule = module {
        single { CivicsApi.civicsApiService() }
        single { LocalDB.createElectionsDao(androidApplication().applicationContext) }
        single {
            ElectionLocalRepository(
                get() as ElectionDao,
                get() as CivicsApiService
            ) as ElectionDataSource
        }
        single { VoterInfoLocalRepository(get() as CivicsApiService) as VoterInfoDataSource }
        single { RepresentativeRepository(get() as CivicsApiService) as RepresentativeDataSource }
    }
}