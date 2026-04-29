package com.example.habittracker.network

import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitLog
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("api/habitlogs")
    suspend fun getLogs(): Response<List<HabitLog>>

    @POST("api/habits")
    suspend fun addHabit(@Body habit: Habit): Response<Habit>

    @DELETE("api/habits/{id}")
    suspend fun deleteHabit(@Path("id") id: Int): Response<Unit>
}