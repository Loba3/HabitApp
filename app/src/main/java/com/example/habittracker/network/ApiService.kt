package com.example.habittracker.network

import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitLog
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class HabitInLog(
    val id: Int,
    val name: String,
    val description: String,
    val logs: List<String> = emptyList()
)

data class HabitLogRequest(
    val id: Int = 0,
    val date: String,
    val completed: Boolean,
    val habitId: Int,
    val habit: HabitInLog
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

    @PUT("api/habits/{id}")
    suspend fun updateHabit(@Path("id") id: Int, @Body habit: Habit): Response<Habit>
}