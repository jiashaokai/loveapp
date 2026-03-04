package com.jk.lovediary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.model.CalendarDay
import com.jk.lovediary.utils.FestivalUtils
import com.nlf.calendar.Solar
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
        private val lunarText = view.findViewById<TextView>(R.id.lunarText)
        val festivalUtils = FestivalUtils()
        val dayOfYear = LocalDate.now().dayOfYear;

        fun bind(day: CalendarDay) {
            if (day.isCurrentMonth){
                dayText.text = day.date.dayOfMonth.toString()
                if (day.date.dayOfYear == dayOfYear){
                    dayText.setTextColor(Color.BLUE)
                }
                val festival = festivalUtils.getFestivalName(day.date.year, day.date.monthValue, day.date.dayOfMonth)
                if (festival != null){
                    lunarText.setText(festival)
                }else{
                    var  solar = Solar(
                        day.date.year,
                        day.date.monthValue,
                        day.date.dayOfMonth
                    ) // 例如 2025 年 5 月 5 日
                    var  lunar = solar.getLunar()

                    val lunarDay = lunar.getDayInChinese() // 初一、初二等
                    val lunarMonth = lunar.getMonthInChinese() // 月份

                    lunarText.setText(if (lunarDay == "初一") lunarMonth + "月" else lunarDay)
                }


                if (day.userAChecked && day.userBChecked){
                    dayText.setBackgroundColor(Color.parseColor("#FFB6C1"));
                    lunarText.setBackgroundColor(Color.parseColor("#FFB6C1"));

                    dayText.setTextColor(Color.parseColor("#FFFFFF"));
                    lunarText.setTextColor(Color.parseColor("#FFFFFF"));
                }else if (day.userAChecked){
                    dayText.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    lunarText.setBackgroundColor(Color.parseColor("#ADD8E6"));

                    dayText.setTextColor(Color.parseColor("#00008B"));
                    lunarText.setTextColor(Color.parseColor("#00008B"));
                }else if (day.userBChecked){
                    dayText.setBackgroundColor(Color.parseColor("#FFD1DC"));
                    lunarText.setBackgroundColor(Color.parseColor("#FFD1DC"));

                    dayText.setTextColor(Color.parseColor("#FF69B4"));
                    lunarText.setTextColor(Color.parseColor("#FF69B4"));
                }else{
                    dayText.setBackgroundColor(Color.TRANSPARENT);
                    lunarText.setBackgroundColor(Color.TRANSPARENT);

                    dayText.setTextColor(Color.BLACK);
                    lunarText.setTextColor(Color.BLACK);
                }

            }else{
                dayText.text = ""
            }
        }
    }
}