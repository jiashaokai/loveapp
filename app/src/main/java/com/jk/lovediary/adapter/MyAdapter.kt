package com.jk.lovediary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    fun submitList(newList: List<CalendarDay>) {
        days.clear()
        days.addAll(newList)
        notifyDataSetChanged()
    }

    private val TYPE_VISIBLE = 0
    private val TYPE_HIDDEN = 1

    private var isHidden = true // 控制部分内容的显示与隐藏

    override fun getItemViewType(position: Int): Int {
        // 这里假设第一个部分是需要隐藏的
        return if (isHidden) TYPE_HIDDEN else TYPE_VISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HIDDEN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_hidden, parent, false)
                HiddenViewHolder(view,onDayClick)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_visible, parent, false)
                VisibleViewHolder(view,onDayClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HiddenViewHolder -> {
                // 设置隐藏部分的内容
                holder.bind(days[position])
            }
            is VisibleViewHolder -> {
                // 设置显示部分的内容
                holder.bind(days[position])
            }
        }
    }

    override fun getItemCount(): Int = days.size

    // 控制显示与隐藏部分
    fun toggleHidden() {
        isHidden = !isHidden
        notifyDataSetChanged() // 更新RecyclerView
    }

    // ViewHolder类
    class VisibleViewHolder(view: View,
                            private val onDayClick: (CalendarDay) -> Unit) : RecyclerView.ViewHolder(view){
        private val dayText = view.findViewById<TextView>(R.id.dayVisibleText)
        private val lunarText = view.findViewById<TextView>(R.id.lunarVisibleText)
        private val userBIcon = view.findViewById<ImageView>(R.id.userVisibleIcon)
        val festivalUtils = FestivalUtils()

        val currentMonth = LocalDate.now().monthValue
        val dayOfYear = LocalDate.now().dayOfYear;
        fun bind(day: CalendarDay) {
            if (day.date.monthValue == currentMonth){

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
                    userBIcon.setImageResource(R.drawable.shuangren);
                }else if (day.userAChecked){
                    userBIcon.setImageResource(R.drawable.yingying);
                }else if (day.userBChecked){
                    userBIcon.setImageResource(R.drawable.jiakun);
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
    class HiddenViewHolder(view: View,
                           private val onDayClick: (CalendarDay) -> Unit) : RecyclerView.ViewHolder(view){
        private val dayText = view.findViewById<TextView>(R.id.dayTextHidden)
        private val lunarText = view.findViewById<TextView>(R.id.lunarTextHidden)


        val festivalUtils = FestivalUtils()

        val currentMonth = LocalDate.now().monthValue
        val dayOfYear = LocalDate.now().dayOfYear;
        fun bind(day: CalendarDay) {
            if (day.date.monthValue == currentMonth){

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