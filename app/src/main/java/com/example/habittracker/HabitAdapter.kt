package com.example.habittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.model.Habit

class HabitAdapter(
    private val habits: List<Habit>,
    private val onChecked: (Habit, Boolean) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.habitText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]

        holder.textView.text = "${habit.name} - ${habit.description}"

        holder.checkBox.setOnCheckedChangeListener(null) // prevent recycle bug
        holder.checkBox.isChecked = false

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onChecked(habit, isChecked)
        }
    }

    override fun getItemCount() = habits.size
}
