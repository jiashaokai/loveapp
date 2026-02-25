package com.jk.lovediary.model.response

data class HttpResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)