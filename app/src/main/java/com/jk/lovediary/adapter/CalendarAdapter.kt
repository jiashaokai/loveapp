//package com.jk.lovediary.ui.theme
//
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.jk.lovediary.R
//import com.jk.lovediary.model.CalendarDay
//import com.nlf.calendar.Solar
//import java.time.LocalDate
//
//
//class CalendarAdapter(
//    private val onDayClick: (CalendarDay) -> Unit,
//) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
//
//    private val days = mutableListOf<CalendarDay>()
//
//    fun submitList(newList: List<CalendarDay>) {
//        days.clear()
//        days.addAll(newList)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_hidden, parent, false)
//
//        return DayViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = days.size
//
//    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
//        holder.bind(days[position])
//    }
//
//    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val dayText = view.findViewById<TextView>(R.id.dayText)
//        private val lunarText = view.findViewById<TextView>(R.id.lunarText)
////        private val userAIcon = view.findViewById<ImageView>(R.id.userAIcon)
////        private val userBIcon = view.findViewById<ImageView>(R.id.userBIcon)
//        private val userBIcon = view.findViewById<ImageView>(R.id.userIcon)
//        val festivalUtils = FestivalUtils()
//
//        val currentMonth = LocalDate.now().monthValue
//        val dayOfYear = LocalDate.now().dayOfYear;
//        fun bind(day: CalendarDay) {
//            if (day.date.monthValue == currentMonth){
//
//                dayText.text = day.date.dayOfMonth.toString()
//                if (day.date.dayOfYear == dayOfYear){
//                    dayText.setTextColor(Color.BLUE)
//                }
//                val festival = festivalUtils.getFestivalName(day.date.year, day.date.monthValue, day.date.dayOfMonth)
//                if (festival != null){
//                    lunarText.setText(festival)
//                }else{
//                    var  solar = Solar(day.date.year, day.date.monthValue, day.date.dayOfMonth) // 例如 2025 年 5 月 5 日
//                    var  lunar = solar.getLunar()
//
//                    val lunarDay = lunar.getDayInChinese() // 初一、初二等
//                    val lunarMonth = lunar.getMonthInChinese() // 月份
//
//                    lunarText.setText(if (lunarDay == "初一") lunarMonth + "月" else lunarDay)
//                }
//                if (day.userAChecked && day.userBChecked){
//                    userBIcon.setImageResource(R.drawable.shuangren);
//                }else if (day.userAChecked){
//                    userBIcon.setImageResource(R.drawable.yingying);
//                }else if (day.userBChecked){
//                    userBIcon.setImageResource(R.drawable.jiakun);
//                }
//                itemView.setOnClickListener {
//                    onDayClick(day)
//                }
//            }
//        }
//    }
//}