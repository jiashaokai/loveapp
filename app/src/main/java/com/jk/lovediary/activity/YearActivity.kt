package com.jk.lovediary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.adapter.YearAdapter
import com.jk.lovediary.utils.generateYearCalendarDays
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class YearActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)


        val gridLayout = findViewById<GridLayout>(R.id.monthGrid)
        val currentDate = LocalDate.now() // 获取当前日期

        for (i in 1..12) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_month, gridLayout, false)

            val tvMonthName = itemView.findViewById<TextView>(R.id.tvMonthName)
            val innerRecyclerView = itemView.findViewById<RecyclerView>(R.id.monthInnerRecyclerView).apply {
                layoutManager = GridLayoutManager(this@YearActivity, 7)
            }

            var adapter = YearAdapter();
            innerRecyclerView.adapter = adapter;

            val firstDay = LocalDate.of(currentDate.year, i, 1)
            val formatter = DateTimeFormatter.ofPattern("yyyy/M") // 定义格式
            val formattedDate = firstDay.format(formatter) // 格式化当前日期

            tvMonthName.text = formattedDate


            val days = generateYearCalendarDays(currentDate.year, i)
            days.forEach { day ->

                day.userAChecked = false
                day.userBChecked = false
            }
            adapter.submitList(days)

            val index = i - 1

            val columnIndex = 0
            val rowIndex = index

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.MATCH_PARENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(columnIndex) // 列索引，跨度1，占比1
                rowSpec = GridLayout.spec(rowIndex)        // 行索引，跨度1，占比1
                setMargins(8, 8, 8, 8)
            }

            gridLayout.addView(itemView, params)
        }
    }
}