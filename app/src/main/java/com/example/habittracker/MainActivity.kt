package com.example.habittracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.network.HabitLogRequest
import com.example.habittracker.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchHabits()
    }

    private fun fetchHabits() {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getHabits()
            if (response.isSuccessful) {
                val habits = response.body() ?: emptyList()

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.adapter = HabitAdapter(habits) { habit, isChecked ->
                    if (isChecked) {
                        markHabitComplete(habit.id)
                    }
                }
            }
        }
    }

    private fun markHabitComplete(habitId: Int) {
        lifecycleScope.launch {
            try {
                val log = HabitLogRequest(
                    date = "2026-04-30",
                    completed = true,
                    habitId = habitId
                )

                RetrofitInstance.api.createLog(log)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}