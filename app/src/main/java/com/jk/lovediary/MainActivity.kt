package com.jk.lovediary

//import com.jk.lovediary.ui.theme.CalendarAdapter


import CalendarPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jk.lovediary.activity.LoginActivity
import com.jk.lovediary.adapter.RankAdapter
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.utils.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit


class MainActivity : FragmentActivity() {

    private var currentYear = 2025
    private var currentMonth = 3
    private var isMonthRank = true

    //登录
    private var loginUserName: String? = null

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化请求
        RetrofitClient.init(applicationContext)

        currentYear = LocalDate.now().year
        currentMonth = LocalDate.now().monthValue

        // 恢复登录状态
        val unloginIcon = findViewById<ImageView>(R.id.unlogin)
        unloginIcon.visibility = View.VISIBLE
        unloginIcon.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        getUserName()

        val viewPager = findViewById<ViewPager2>(R.id.calendarViewPager)

        viewPager.adapter = CalendarPagerAdapter(this)

        // 默认跳到当前月
        val startYearMonth = YearMonth.of(2020, 1)
        val currentYearMonth = YearMonth.now()

        findViewById<TextView>(R.id.yearAndMouthText).text =
            "${currentYearMonth.year}-${currentYearMonth.monthValue}"

        val position = ChronoUnit.MONTHS
            .between(startYearMonth, currentYearMonth)
            .toInt()

        viewPager.setCurrentItem(position, false)
        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val yearMonth = startYearMonth.plusMonths(position.toLong())

                    // 更新标题
                    findViewById<TextView>(R.id.yearAndMouthText).text =
                        "${yearMonth.year}-${yearMonth.monthValue}"

//                    // 根据月份刷新统计
//                    updateCheckInCountsByMonth(yearMonth)
                }
            }
        )


        updateCheckInCounts()

        recyclerView = findViewById(R.id.rankRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val group  = findViewById<RadioGroup>(R.id.rankTypeGroup)

        group.setOnCheckedChangeListener { _, checkedId ->

            if (checkedId == R.id.monthRankBtn) {

                // 月排行
                isMonthRank = true
                updateDateText()
                loadRankData()
            } else if (checkedId == R.id.yearRankBtn) {

                // 年排行
                isMonthRank = false
                updateDateText()
                loadRankData()

            }
        }

        findViewById<ImageView>(R.id.prevBtn).setOnClickListener {

            if (isMonthRank) {

                currentMonth--

                if (currentMonth < 1) {
                    currentMonth = 12
                    currentYear--
                }

            } else {
                currentYear--
            }

            updateDateText()
            loadRankData()
        }

        findViewById<ImageView>(R.id.nextBtn).setOnClickListener {

            if (isMonthRank) {

                currentMonth++

                if (currentMonth > 12) {
                    currentMonth = 1
                    currentYear++
                }

            } else {
                currentYear++
            }

            updateDateText()
            loadRankData()
        }

        updateDateText()
        loadRankData()
    }

    private fun updateDateText() {
        val rankDateText = findViewById<TextView>(R.id.rankDateText)
        if (isMonthRank) {
            rankDateText.text = "${currentYear}年${currentMonth}月"
        } else {
            rankDateText.text = "${currentYear}年"
        }
    }

    private fun loadRankData() {
        lifecycleScope.launch {
            var time: String
            if (isMonthRank) {
                time = String.format("%d-%02d", currentYear, currentMonth)
            } else {
                time = currentYear.toString()
            }

            try {
                val response = RetrofitClient.instance.getTop(time);

                val recordList = response.data


                recyclerView.adapter = RankAdapter(recordList)
            }catch (e: Exception) {
                Log.e("网络异常", e.toString())
                // 这里防止崩溃
            }
        }
    }

//    private fun updateCheckInCountsByMonth(yearMonth: YearMonth) {
//
//        lifecycleScope.launch {
//            try {
//
//                val ym = yearMonth.toString() // 2025-03
//
//                val response = RetrofitClient.instance.getCount()
//
//                val vo = response.data
//
//                findViewById<TextView>(R.id.myMonthText).text =
//                    "👨 本月打卡  ${vo.myMonthTotal} 天"
//
//                findViewById<TextView>(R.id.relatedMonthText).text =
//                    "👩 本月打卡  ${vo.relatedMonthTotal} 天"
//
//                findViewById<TextView>(R.id.togetherMonthText).text =
//                    "💕 本月共同  ${vo.togetherMonthTotal} 天"
//
//            } catch (e: Exception) {
//                Log.e("月份刷新异常", e.toString())
//            }
//        }
//    }

    private fun updateCheckInCounts() {
        lifecycleScope.launch {
            try {

                val response = RetrofitClient.instance.getCount();

                val vo = response.data

//                // ===== 本月 =====
                val myMonth = vo.myMonthTotal
                val relatedMonth = vo.relatedMonthTotal
                val togetherMonth = vo.togetherMonthTotal
//
//                findViewById<TextView>(R.id.myMonthText).text =
//                    "本人本月打卡  $myMonth 天"
//
//                findViewById<TextView>(R.id.relatedMonthText).text =
//                    "对方本月打卡  $relatedMonth 天"
//
//                findViewById<TextView>(R.id.togetherMonthText).text =
//                    "💕 本月共同  $togetherMonth 天"


                // ===== 本年度 =====
                val myYear = vo.myYearTotal
                val relatedYear = vo.relatedYearTotal
                val togetherYear = vo.togetherYearTotal

                findViewById<TextView>(R.id.myYearText).text =
                    "本人年度打卡  $myYear 天  本月打卡  $myMonth 天"

                findViewById<TextView>(R.id.relatedYearText).text =
                    "对方年度打卡  $relatedYear 天 本月打卡  $relatedMonth 天"

                findViewById<TextView>(R.id.togetherYearText).text =
                    "💕 年度共同  $togetherYear 天 本月共同  $togetherMonth 天"


//                var monthStatRecyclerView = findViewById<RecyclerView>(R.id.monthStatRecyclerView)
//                monthStatRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
//                monthStatRecyclerView.adapter = MonthStatAdapter(vo.monthStats)
            } catch (e: Exception) {
                Log.e("网络异常", e.toString())
                // 这里防止崩溃
            }
        }
    }

    // 如果登录后返回主页，更新显示
    override fun onResume() {
        super.onResume()
        getUserName()
    }

    fun getUserName() {

        val call = RetrofitClient.instance.getUserName()
        call.enqueue(object : Callback<HttpResponse<String>> {
            override fun onResponse(
                call: Call<HttpResponse<String>>,
                response: Response<HttpResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.code == 200) {
                        loginUserName = result?.data.toString()
                        updateUser()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "请求失败: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "请求失败: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun updateUser() {
        if (loginUserName != "") {

            val welcomeText = findViewById<TextView>(R.id.welcome)

            welcomeText.text = "$loginUserName"
            welcomeText.visibility = View.VISIBLE

            val unloginIcon = findViewById<ImageView>(R.id.unlogin)
            unloginIcon.visibility = View.GONE
        }
    }
}