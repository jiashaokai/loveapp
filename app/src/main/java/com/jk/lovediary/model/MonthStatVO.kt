package com.jk.lovediary.model

data class MonthStatVO (
    var yearMonth: String,
    var myCount: Long,
    var relatedCount: Long,
    var togetherCount: Long
)