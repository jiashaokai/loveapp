package com.jk.lovediary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.model.CalendarDay
import com.jk.lovediary.utils.FestivalUtils
import com.nlf.calendar.Solar
import java.time.LocalDate

class MyAdapter (
    private val onDayClick: (CalendarDay) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val days = mutableListOf<CalendarDay>()

    private var current = 0

    fun submitList(newList: List<CalendarDay>, month: Int) {
        days.clear()
        days.addAll(newList)
        current = month
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_hidden, parent, false)
        return  HiddenViewHolder(view,onDayClick,current)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HiddenViewHolder -> {
                // 设置隐藏部分的内容
                holder.bind(days[position])
            }
        }
    }

    override fun getItemCount(): Int = days.size

    class HiddenViewHolder(
        view: View,
        private val onDayClick: (CalendarDay) -> Unit,
        private val current: Int
    ) : RecyclerView.ViewHolder(view){
        private val dayText = view.findViewById<TextView>(R.id.dayTextHidden)
        private val lunarText = view.findViewById<TextView>(R.id.lunarTextHidden)
        private val dayOverall = view.findViewById<LinearLayout>(R.id.dayOverall)

        private val isBoy = true


        val festivalUtils = FestivalUtils()


        val dayOfYear = LocalDate.now().dayOfYear;
        fun bind(day: CalendarDay) {
            if (day.date.monthValue == current){

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
//                    dayText.setBackgroundColor(Color.parseColor("#FFB6C1"));
//                    lunarText.setBackgroundColor(Color.parseColor("#FFB6C1"));

                    dayOverall.setBackgroundResource(R.drawable.all_check)

                    dayText.setTextColor(Color.parseColor("#C71585"));
                    lunarText.setTextColor(Color.parseColor("#C71585"));
                }else if (day.userAChecked){
//                    dayText.setBackgroundColor(Color.parseColor("#FFD1DC"));
//                    lunarText.setBackgroundColor(Color.parseColor("#FFD1DC"));

                    if (isBoy){
                        dayOverall.setBackgroundResource(R.drawable.girl_check)

                        dayText.setTextColor(Color.parseColor("#FF69B4"));
                        lunarText.setTextColor(Color.parseColor("#FF69B4"));
                    }else{
                        dayOverall.setBackgroundResource(R.drawable.boy_check)

                        dayText.setTextColor(Color.parseColor("#00008B"));
                        lunarText.setTextColor(Color.parseColor("#00008B"));
                    }


                }else if (day.userBChecked){

//                    dayText.setBackgroundColor(Color.parseColor("#ADD8E6"));
//                    lunarText.setBackgroundColor(Color.parseColor("#ADD8E6"));

                    if (isBoy){
                        dayOverall.setBackgroundResource(R.drawable.boy_check)

                        dayText.setTextColor(Color.parseColor("#00008B"));
                        lunarText.setTextColor(Color.parseColor("#00008B"));
                    }else{
                        dayOverall.setBackgroundResource(R.drawable.girl_check)

                        dayText.setTextColor(Color.parseColor("#FF69B4"));
                        lunarText.setTextColor(Color.parseColor("#FF69B4"));
                    }


                }else{
//                    dayText.setBackgroundColor(Color.TRANSPARENT);
//                    lunarText.setBackgroundColor(Color.TRANSPARENT);

                    dayOverall.setBackgroundResource(R.drawable.no_check)


                    dayText.setTextColor(Color.BLACK);
                    lunarText.setTextColor(Color.BLACK);
                }


                itemView.setOnClickListener {
                    onDayClick(day)
                }
            }else{
                dayText.text = ""
                lunarText.text = ""
            }
        }
    }
}