package com.jk.lovediary

//import com.jk.lovediary.ui.theme.CalendarAdapter
import com.jk.lovediary.adapter.MyAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.activity.LoginActivity
import com.jk.lovediary.activity.YearActivity
import com.jk.lovediary.data.CheckInStore
import com.jk.lovediary.model.CalendarDay
import com.jk.lovediary.model.RecordVO
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.utils.RetrofitClient
import com.jk.lovediary.utils.generateCalendarDays
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    private lateinit var adapter: MyAdapter
    private lateinit var store: CheckInStore
    private lateinit var recyclerView: RecyclerView

    private var initialY = 0f

    //登录
    private var loginUserName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化请求
        RetrofitClient.init(applicationContext)

        store = CheckInStore(this)

        adapter = MyAdapter { day -> showCheckInDialog(day) }

        recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 7)
            adapter = this@MainActivity.adapter
        }


        val currentDate = LocalDate.now() // 获取当前日期
        val formatter = DateTimeFormatter.ofPattern("yyyy/M") // 定义格式
        val formattedDate = currentDate.format(formatter) // 格式化当前日期

        val yearAndMouthText = findViewById<TextView>(R.id.yearAndMouthText).apply {
            text = formattedDate;
        }
        yearAndMouthText.setOnClickListener {
            val intent = Intent(this, YearActivity::class.java)
            startActivity(intent)
        }


        val days = generateCalendarDays(currentDate.year, currentDate.monthValue)
        lifecycleScope.launch {

            days.forEach { day ->
                day.userAChecked = false
                day.userBChecked = false
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM") // 定义格式

            val time = currentDate.format(formatter) // 格式化当前日期
            try {
                val response = RetrofitClient.instance.getByRecord(time);

                val recordList = response.data

                val recordMap = recordList.associateBy { LocalDate.parse(it.time) }  // 注意 time 格式是 yyyy-MM-dd

                days.forEach { day ->
                    val record = recordMap[day.date]
                    if (record != null) {
                        day.userAChecked = record.relatedUserStatus
                        day.userBChecked = record.myStatus
                    }else{
                        day.userAChecked = false
                        day.userBChecked = false
                    }
                }
            }catch (e: Exception) {
                Log.e("网络异常", e.toString())
                // 这里防止崩溃
            }

            adapter.submitList(days)
            updateCheckInCounts()
        }



        // 恢复登录状态
        val unloginIcon = findViewById<ImageView>(R.id.unlogin)
        unloginIcon.visibility = View.VISIBLE
        unloginIcon.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        getUserName()



    }

    private fun showCheckInDialog(day: CalendarDay) {
        val options = arrayOf("打卡", "清除当天签到数据")
        AlertDialog.Builder(this)
            .setTitle("选择打卡用户")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 定义格式
                        val formattedDate = day.date.format(formatter) // 格式化当前日期

                        check(formattedDate, day)
                    }
                    1 -> {

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 定义格式
                        val formattedDate = day.date.format(formatter) // 格式化当前日期

                        deleteRecord(formattedDate, day)
                    }
                }
            }
            .show()
    }

    private fun showRecyclerViewForDownwardSwipe() {
        // 向下滑动时显示不同的 RecyclerView 内容
        adapter?.toggleHidden()
        recyclerView?.smoothScrollToPosition(0)  // 可选的平滑滚动
    }

    private fun showRecyclerViewForUpwardSwipe() {
        // 向上滑动时显示不同的 RecyclerView 内容
        adapter?.toggleHidden()
        recyclerView?.smoothScrollToPosition(0)  // 可选的平滑滚动
    }

    // 重写 dispatchTouchEvent 来监听所有触摸事件
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录触摸的初始Y坐标
                initialY = event.rawY
            }


            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val deltaY = event.rawY
                if (deltaY > initialY){
                    showRecyclerViewForUpwardSwipe()
                }else if (deltaY < initialY){
                    showRecyclerViewForDownwardSwipe()
                }
            }
        }
        return super.dispatchTouchEvent(event)  // 继续传递事件给其他组件
    }

    private fun updateCheckInCounts() {
        lifecycleScope.launch {
            val days = adapter.days // 获取当前显示的日历天数
            var userACount = 0
            var userBCount = 0
            days.forEach { day ->
                if (day.userAChecked) userACount++
                if (day.userBChecked) userBCount++
            }

            val part1 = "运动： "
            val part2 = "贾坤 "
            val part3 = "莹莹 "
            val part4 = "次 "
            findViewById<TextView>(R.id.dakaCount).apply {
                val stringBuilder = StringBuilder()
                stringBuilder.append(part1)
                stringBuilder.append(part3).append(userACount).append(part4)
                stringBuilder.append(part2).append(userBCount).append(part4)
                text = stringBuilder.toString()
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
            override fun onResponse(call: Call<HttpResponse<String>>, response: Response<HttpResponse<String>>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.code == 200) {
                        loginUserName = result?.data.toString()
                        updateUser()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "请求失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "请求失败: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun check(time: String,day: CalendarDay) {

        val call = RetrofitClient.instance.check(time);
        call.enqueue(object : Callback<HttpResponse<Boolean>> {
            override fun onResponse(call: Call<HttpResponse<Boolean>>, response: Response<HttpResponse<Boolean>>) {
                if (response.isSuccessful) {
                    day.userBChecked = true
                    store.setCheckInStatus(day.date, "A", true)

                    adapter.notifyDataSetChanged()
                    updateCheckInCounts()
                } else {
                    Toast.makeText(this@MainActivity, "请求服务器失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<Boolean>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "请求服务器失败: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteRecord(time: String,day: CalendarDay) {

        val call = RetrofitClient.instance.delete(time);
        call.enqueue(object : Callback<HttpResponse<Boolean>> {
            override fun onResponse(call: Call<HttpResponse<Boolean>>, response: Response<HttpResponse<Boolean>>) {
                if (response.isSuccessful) {
                    day.userBChecked = false
                    store.clearCheckInStatus(day.date, "A")

                    adapter.notifyDataSetChanged()
                    updateCheckInCounts()
                } else {
                    Toast.makeText(this@MainActivity, "请求服务器失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<Boolean>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "请求服务器失败: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUser() {
        if (loginUserName != "") {

            val welcomeText = findViewById<TextView>(R.id.welcome)

            welcomeText.text = "欢迎，$loginUserName"
            welcomeText.visibility = View.VISIBLE

            val unloginIcon = findViewById<ImageView>(R.id.unlogin)
            unloginIcon.visibility = View.GONE
        }
    }
}