package com.example.habittracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.model.Habit
import com.example.habittracker.network.HabitInLog
import com.example.habittracker.network.HabitLogRequest
import com.example.habittracker.network.RetrofitInstance
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupDateStrip()

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            showAddDialog()
        }

        fetchHabits()
    }

    private fun setupDateStrip() {
        val dates = (-3..3).map { LocalDate.now().plusDays(it.toLong()) }
        val dateRecyclerView = findViewById<RecyclerView>(R.id.dateRecyclerView)
        dateRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dateRecyclerView.adapter = DateAdapter(dates, selectedDate) { date ->
            selectedDate = date
            fetchHabits()
        }
    }

    private fun fetchHabits() {
        lifecycleScope.launch {
            val habitsResponse = RetrofitInstance.api.getHabits()
            val logsResponse = RetrofitInstance.api.getLogs()

            if (habitsResponse.isSuccessful && logsResponse.isSuccessful) {

                val habits = habitsResponse.body() ?: emptyList()
                val logs = logsResponse.body() ?: emptyList()

                val today = selectedDate.toString()

                val completedToday = logs
                    .filter { log -> log.date.take(10) == today }
                    .groupBy { it.habitId }
                    .filter { (_, entries) -> entries.maxByOrNull { it.id }?.completed == true }
                    .keys
                    .toList()

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                recyclerView.adapter = HabitAdapter(
                    habits,
                    completedToday,
                    onChecked = { habit, isChecked ->
                        if (isChecked) markHabitComplete(habit)
                        else markHabitIncomplete(habit)
                    },
                    onDelete = { habit ->
                        deleteHabit(habit.id)
                    },
                    onEdit = { habit ->
                        showEditDialog(habit)
                    }
                )
            }
        }
    }

    private fun markHabitComplete(habit: Habit) {
        lifecycleScope.launch {
            try {
                val log = HabitLogRequest(
                    date = selectedDate.toString() + "T00:00:00Z",
                    completed = true,
                    habitId = habit.id,
                    habit = HabitInLog(habit.id, habit.name, habit.description)
                )
                val response = RetrofitInstance.api.createLog(log)
                if (response.isSuccessful) {
                    fetchHabits()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "no body"
                    Log.e("HabitTracker", "createLog failed ${response.code()}: $errorBody")
                    Toast.makeText(this@MainActivity, "Error ${response.code()}: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun markHabitIncomplete(habit: Habit) {
        lifecycleScope.launch {
            try {
                val log = HabitLogRequest(
                    date = selectedDate.toString() + "T00:00:00Z",
                    completed = false,
                    habitId = habit.id,
                    habit = HabitInLog(habit.id, habit.name, habit.description)
                )
                val response = RetrofitInstance.api.createLog(log)
                if (response.isSuccessful) {
                    fetchHabits()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "no body"
                    Log.e("HabitTracker", "createLog failed ${response.code()}: $errorBody")
                    Toast.makeText(this@MainActivity, "Error ${response.code()}: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showAddDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 0)
        }
        val nameInput = EditText(this).apply { hint = "Name" }
        val descInput = EditText(this).apply { hint = "Description" }
        layout.addView(nameInput)
        layout.addView(descInput)

        AlertDialog.Builder(this)
            .setTitle("New Habit")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val desc = descInput.text.toString().trim()
                if (name.isNotEmpty()) {
                    addHabit(name, desc)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(habit: Habit) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 0)
        }
        val nameInput = EditText(this).apply {
            hint = "Name"
            setText(habit.name)
        }
        val descInput = EditText(this).apply {
            hint = "Description"
            setText(habit.description)
        }
        layout.addView(nameInput)
        layout.addView(descInput)

        AlertDialog.Builder(this)
            .setTitle("Edit Habit")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val newName = nameInput.text.toString().trim()
                val newDesc = descInput.text.toString().trim()
                if (newName.isNotEmpty()) {
                    editHabit(habit.id, newName, newDesc)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun editHabit(id: Int, name: String, description: String) {
        lifecycleScope.launch {
            RetrofitInstance.api.updateHabit(id, Habit(id, name, description))
            fetchHabits()
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

    private fun getNow(): String {
        return Instant.now().toString()
    }

}