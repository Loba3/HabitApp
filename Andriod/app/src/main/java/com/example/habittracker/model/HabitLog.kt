package com.example.habittracker.model

data class HabitLog(
    val id: Int,
    val date: String,
    val completed: Boolean,
    val habitId: Int
)
