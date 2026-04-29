package com.example.habittracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.habittracker.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchHabits()
    }

    private fun fetchHabits() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getHabits()
                if (response.isSuccessful) {
                    val habits = response.body()
                    Log.d("API", habits.toString())
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Exception: ${e.message}")
            }
        }
    }
}