package com.example.habittracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.model.Habit
import com.example.habittracker.network.HabitLogRequest
import com.example.habittracker.network.RetrofitInstance
import java.time.LocalDate
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            addHabit("New Habit", "Description")
        }

        fetchHabits()
    }

    private fun fetchHabits() {
        lifecycleScope.launch {
            val habitsResponse = RetrofitInstance.api.getHabits()
            val logsResponse = RetrofitInstance.api.getLogs()

            if (habitsResponse.isSuccessful && logsResponse.isSuccessful) {

                val habits = habitsResponse.body() ?: emptyList()
                val logs = logsResponse.body() ?: emptyList()

                val today = getToday()

                val completedToday = logs
                    .filter { it.date.startsWith(today) && it.completed }
                    .map { it.habitId }

                val progressText = findViewById<TextView>(R.id.progressText)
                val completedCount = completedToday.size
                val total = habits.size
                progressText.text = "$completedCount / $total habits completed"

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                recyclerView.adapter = HabitAdapter(
                    habits,
                    completedToday,
                    onChecked = { habit, isChecked ->
                        if (isChecked) markHabitComplete(habit.id)
                    },
                    onDelete = { habit ->
                        deleteHabit(habit.id)
                    }
                )
            }
        }
    }

    private fun markHabitComplete(habitId: Int) {
        lifecycleScope.launch {
            try {
                val log = HabitLogRequest(
                    date = getToday(),
                    completed = true,
                    habitId = habitId
                )

                RetrofitInstance.api.createLog(log)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteHabit(id: Int) {
        lifecycleScope.launch {
            RetrofitInstance.api.deleteHabit(id)
            fetchHabits()
        }
    }

    private fun addHabit(name: String, description: String) {
        lifecycleScope.launch {
            RetrofitInstance.api.addHabit(
                Habit(0, name, description)
            )
            fetchHabits() // refresh list
        }
    }

    private fun getToday(): String {
        return LocalDate.now().toString()
    }
}