package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object CivicsApi {

    private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

    private val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(ElectionAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    fun civicsApiService(): CivicsApiService{
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(CivicsHttpClient.getClient())
                .baseUrl(BASE_URL)
                .build().create(CivicsApiService::class.java)
    }

}
