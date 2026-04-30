package com.example.habittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DateAdapter(
    private val dates: List<LocalDate>,
    private var selectedDate: LocalDate,
    private val onDateSelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: LinearLayout = view.findViewById(R.id.dateContainer)
        val dayName: TextView = view.findViewById(R.id.dayName)
        val dayNumber: TextView = view.findViewById(R.id.dayNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dates[position]
        val isSelected = date == selectedDate

        holder.dayName.text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        holder.dayNumber.text = date.dayOfMonth.toString()

        holder.container.setBackgroundResource(
            if (isSelected) R.drawable.date_item_bg_selected
            else R.drawable.date_item_bg
        )
        holder.dayName.setTextColor(
            if (isSelected) 0xFFFFFFFF.toInt() else 0xFF888888.toInt()
        )
        holder.dayNumber.setTextColor(
            if (isSelected) 0xFFFFFFFF.toInt() else 0xFF1B1B1B.toInt()
        )

        holder.itemView.setOnClickListener {
            val previous = selectedDate
            selectedDate = date
            notifyItemChanged(dates.indexOf(previous))
            notifyItemChanged(position)
            onDateSelected(date)
        }
    }

    override fun getItemCount() = dates.size
}
