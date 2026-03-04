package com.jk.lovediary.utils

import com.nlf.calendar.Solar
import java.util.Calendar

class FestivalUtils {

    // 判断是否是母亲节
    fun isMothersDay(year: Int, month: Int, day: Int): Boolean {
        if (month != 5){
            return false
        }
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)  // 设置为目标月份的第一天

        var count = 0
        while (calendar.get(Calendar.MONTH) == month - 1) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                count++
                if (count == 2) break
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.get(Calendar.DAY_OF_MONTH) == day
    }

    // 判断是否是父亲节
    fun isFathersDay(year: Int, month: Int, day: Int): Boolean {
        if (month != 6){
            return false
        }
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)  // 设置为目标月份的第一天

        var count = 0
        while (calendar.get(Calendar.MONTH) == month - 1) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                count++
                if (count == 3) break
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.get(Calendar.DAY_OF_MONTH) == day
    }

    // 判断是否是感恩节（11月的第4个星期四）
    fun isThanksgiving(year: Int, month: Int, day: Int): Boolean {
        if (month != 11){
            return false
        }
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)

        var count = 0
        while (calendar.get(Calendar.MONTH) == month - 1) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                count++
                if (count == 4) break
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.get(Calendar.DAY_OF_MONTH) == day
    }

    // 判断是否是复活节
    fun isEaster(year: Int, month: Int, day: Int): Boolean {
        // 复活节的计算方式比较复杂，下面是复活节日期的标准计算方法
        val a = year % 19
        val b = year / 100
        val c = year % 100
        val d = b / 4
        val e = b % 4
        val f = (b + 8) / 25
        val g = (b - f + 1) / 3
        val h = (19 * a + b - d - g + 15) % 30
        val i = c / 4
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = (a + 11 * h + 22 * l) / 451
        val monthOfEaster = (h + l - 7 * m + 114) / 31
        val dayOfEaster = ((h + l - 7 * m + 114) % 31) + 1

        return monthOfEaster == month && dayOfEaster == day
    }

    // 判断是否是情人节
    fun isValentinesDay(year: Int, month: Int, day: Int): Boolean {
        return month == 2 && day == 14
    }

    // 判断是否是劳动节
    fun isLabourDay(year: Int, month: Int, day: Int): Boolean {
        return month == 5 && day == 1
    }

    // 判断是否是圣诞节
    fun isChristmas(year: Int, month: Int, day: Int): Boolean {
        return month == 12 && day == 25
    }

    // 判断是否是儿童节
    fun isChildrensDay(year: Int, month: Int, day: Int): Boolean {
        return month == 6 && day == 1
    }

    // 获取节日名称
    fun getFestivalName(year: Int, month: Int, day: Int): String? {
        val solar = Solar(year, month, day)
        val lunar = solar.lunar

        // 检查农历节日
        lunar.festivals?.let { if (it.isNotEmpty()) return it.joinToString("、") }
//        lunar.otherFestivals?.let { if (it.isNotEmpty()) return it.joinToString("、") }
        lunar.jieQi?.let { if (it.isNotEmpty()) return it }

        // 检查浮动节日
        if (isMothersDay(year, month, day)) return "母亲节"
        if (isFathersDay(year, month, day)) return "父亲节"
        if (isThanksgiving(year, month, day)) return "感恩节"
        if (isEaster(year, month, day)) return "复活节"
        if (isValentinesDay(year, month, day)) return "情人节"
        if (isLabourDay(year, month, day)) return "劳动节"
        if (isChristmas(year, month, day)) return "圣诞节"
        if (isChildrensDay(year, month, day)) return "儿童节"

        return null // 不是节日
    }
}