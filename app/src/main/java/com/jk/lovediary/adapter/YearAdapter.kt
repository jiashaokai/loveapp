package com.jk.lovediary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.model.CalendarDay
import java.time.LocalDate

class YearAdapter(): RecyclerView.Adapter<YearAdapter.YearViewHolder>()  {

    val days = mutableListOf<CalendarDay>()

    fun submitList(newList: List<CalendarDay>) {
        days.clear()
        days.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_year_calendar_day, parent, false)
        return YearViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: YearViewHolder,
        position: Int
    ) {
        holder.bind(days[position])
    }


    override fun getItemCount(): Int = days.size

    // ViewHolder类
    inner class YearViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val dayText = view.findViewById<TextView>(R.id.dayText)

        val dayOfYear = LocalDate.now().dayOfYear;

        fun bind(day: CalendarDay) {
            if (day.isCurrentMonth){
                dayText.text = day.date.dayOfMonth.toString()
                if (day.date.dayOfYear == dayOfYear){
                    dayText.setTextColor(Color.BLUE)
                }
            }else{
                dayText.text = ""
            }
        }
    }
}