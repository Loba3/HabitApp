package com.example.habittracker.network

import com.example.habittracker.model.Habit
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/habits")
    suspend fun getHabits(): Response<List<Habit>>
}