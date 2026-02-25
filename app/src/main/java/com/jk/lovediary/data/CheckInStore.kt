package com.jk.lovediary.data

import android.content.Context
import java.time.LocalDate

class CheckInStore(context: Context) {
    private val prefs = context.getSharedPreferences("checkin_prefs", Context.MODE_PRIVATE)

    fun getCheckInStatus(date: LocalDate, user: String): Boolean {
        val key = "${date}_$user"
        return prefs.getBoolean(key, false)
    }

    fun setCheckInStatus(date: LocalDate, user: String, status: Boolean) {
        val key = "${date}_$user"
        prefs.edit().putBoolean(key, status).apply()
    }

    fun clearCheckInStatus(date: LocalDate, user: String) {
        val key = "${date}_$user"
        prefs.edit().remove(key).apply() // 清除特定日期和用户的签到状态
    }

    fun setLoginToken(userName: String){
        val key = "token"
        prefs.edit().putString(key,userName).apply() // 清除特定日期和用户的签到状态
    }

    fun getLoginToken(): String {
        val key = "token"
        return prefs.getString(key, "").toString() // 清除特定日期和用户的签到状态
    }
}
