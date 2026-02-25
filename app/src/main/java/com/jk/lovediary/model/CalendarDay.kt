package com.jk.lovediary.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    //莹莹
    var userAChecked: Boolean = false,
    //贾坤
    var userBChecked: Boolean = false
)
