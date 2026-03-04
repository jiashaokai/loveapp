package com.jk.lovediary.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.adapter.MyAdapter
import com.jk.lovediary.data.CheckInStore
import com.jk.lovediary.model.CalendarDay
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.utils.RetrofitClient
import com.jk.lovediary.utils.generateCalendarDays
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MonthFragment : Fragment()  {

    private lateinit var adapter: MyAdapter
    private lateinit var store: CheckInStore

    companion object {
        fun newInstance(year: Int, month: Int): MonthFragment {
            val fragment = MonthFragment()
            val bundle = Bundle()
            bundle.putInt("year", year)
            bundle.putInt("month", month)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        store = CheckInStore(requireContext())

        adapter = MyAdapter { day -> showCheckInDialog(day) }


        val view = inflater.inflate(R.layout.fragment_month, container, false)

        val year = requireArguments().getInt("year")
        val month = requireArguments().getInt("month")

        val recyclerView = view.findViewById<RecyclerView>(R.id.calendarRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        recyclerView.adapter = adapter
        // 加载该月数据
        val yearMonth = YearMonth.of(year, month)
        val days = generateCalendarDays(yearMonth.year,yearMonth.monthValue)

        lifecycleScope.launch {

            days.forEach { day ->
                day.userAChecked = false
                day.userBChecked = false
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM") // 定义格式


            val time = formatter.format(yearMonth) // 格式化当前日期
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

            adapter.submitList(days,month)
        }
        return view
    }

    private fun showCheckInDialog(day: CalendarDay) {
        val options = arrayOf("打卡", "清除当天签到数据")
        AlertDialog.Builder(requireContext())
            .setTitle("选择")
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

    fun check(time: String,day: CalendarDay) {

        val call = RetrofitClient.instance.check(time);
        call.enqueue(object : Callback<HttpResponse<Boolean>> {
            override fun onResponse(call: Call<HttpResponse<Boolean>>, response: Response<HttpResponse<Boolean>>) {
                if (response.isSuccessful) {
                    day.userBChecked = true
                    store.setCheckInStatus(day.date, "A", true)

                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "请求服务器失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<Boolean>>, t: Throwable) {
                Toast.makeText(requireContext(), "请求服务器失败: ${t.message}", Toast.LENGTH_SHORT).show()
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
                } else {
                    Toast.makeText(requireContext(), "请求服务器失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HttpResponse<Boolean>>, t: Throwable) {
                Toast.makeText(requireContext(), "请求服务器失败: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}