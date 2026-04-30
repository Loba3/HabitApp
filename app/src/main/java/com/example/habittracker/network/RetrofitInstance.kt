package com.example.habittracker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://habittrack-api-b7atdygbd0dhc5dq.uksouth-01.azurewebsites.net/"
    // 👆 this connects emulator → your local API

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}