package com.jk.lovediary.model

data class YearStatVO (
    var myYearTotal: Long = 0,
    var relatedYearTotal: Long = 0,
    var togetherYearTotal: Long = 0,

    var myMonthTotal: Long = 0,
    var relatedMonthTotal: Long = 0,
    var togetherMonthTotal: Long = 0,

    var monthStats: List<MonthStatVO> = emptyList()
)