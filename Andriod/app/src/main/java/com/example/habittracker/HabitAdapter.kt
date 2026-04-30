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
    private val completedToday: List<Int>,
    private val onChecked: (Habit, Boolean) -> Unit,
    private val onDelete: (Habit) -> Unit,
    private val onEdit: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.habitText)
        val descView: TextView = view.findViewById(R.id.habitDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]

        holder.textView.text = habit.name
        holder.descView.text = habit.description

        holder.checkBox.setOnCheckedChangeListener(null)

        // THIS IS THE KEY LINE
        holder.checkBox.isChecked = completedToday.contains(habit.id)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onChecked(habit, isChecked)
        }

        holder.itemView.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }

        holder.itemView.setOnLongClickListener {
            val popup = android.widget.PopupMenu(holder.itemView.context, holder.itemView)
            popup.menu.add("Edit")
            popup.menu.add("Delete")
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit" -> onEdit(habit)
                    "Delete" -> onDelete(habit)
                }
                true
            }
            popup.show()
            true
        }
    }

    override fun getItemCount() = habits.size
}
