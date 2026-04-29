package com.example.habittracker.network

import com.example.habittracker.model.Habit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class HabitLogRequest(
    val date: String,
    val completed: Boolean,
    val habitId: Int
)

interface ApiService {

    @GET("api/habits")
    suspend fun getHabits(): Response<List<Habit>>

    @POST("api/habitlogs")
    suspend fun createLog(@Body log: HabitLogRequest): Response<Unit>
}