package com.jk.lovediary.utils

import com.jk.lovediary.model.CalendarDay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun generateCalendarDays(year: Int, month: Int): List<CalendarDay> {
    val firstDay = LocalDate.of(year, month, 1)
    val startOfCalendar = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val today = LocalDate.now()

    val mutableList = mutableListOf<CalendarDay>()

    for (i in 0..42) {
        val date = startOfCalendar.plusDays(i.toLong())

        if (date.monthValue <= month){
            mutableList.add(CalendarDay(
                date = date,
                isCurrentMonth = date.monthValue == month,
                isToday = date == today
            ))
        }
    }


    return mutableList
}

fun generateYearCalendarDays(year: Int, month: Int): List<CalendarDay> {
    val firstDay = LocalDate.of(year, month, 1)
    val startOfCalendar = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val today = LocalDate.now()

    val mutableList = mutableListOf<CalendarDay>()

    for (i in 0..42) {
        val date = startOfCalendar.plusDays(i.toLong())


        mutableList.add(CalendarDay(
            date = date,
            isCurrentMonth = date.monthValue == month,
            isToday = date == today
        ))

    }


    return mutableList
}
