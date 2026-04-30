package com.example.habittracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.model.Habit
import com.example.habittracker.network.HabitLogRequest
import com.example.habittracker.network.RetrofitInstance
import java.time.LocalDate
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            showAddDialog()
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
                    },
                    onEdit = { habit ->
                        showEditDialog(habit)
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
}