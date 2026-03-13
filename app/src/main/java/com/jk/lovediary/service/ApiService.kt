package com.jk.lovediary.service

import com.jk.lovediary.model.NoteVO
import com.jk.lovediary.model.RecordTopVO
import com.jk.lovediary.model.RecordVO
import com.jk.lovediary.model.YearStatVO
import com.jk.lovediary.model.param.LoginParam
import com.jk.lovediary.model.param.NoteParam
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.model.param.UserCreateParam
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/jk/auth")
    fun getUserName(): Call<HttpResponse<String>>

    @POST("/jk/auth/register")
    fun register(@Body param: UserCreateParam): Call<HttpResponse<Boolean>>

    @POST("/jk/auth/login")
    fun login(@Body param: LoginParam): Call<HttpResponse<String>>

    @PUT("/jk/auth/related/{phoneNum}")
    fun relatedUser(@Path("phoneNum") phoneNum: String): Call<HttpResponse<Boolean>>

    @POST("/jk/record/{time}")
    fun check(@Path("time") time: String): Call<HttpResponse<Boolean>>

    @POST("/jk/record/{time}/delete")
    fun delete(@Path("time") time: String): Call<HttpResponse<Boolean>>


    @GET("/jk/record/{time}")
    suspend fun getByRecord(@Path("time") time: String): HttpResponse<List<RecordVO>>

    @GET("/jk/record/count")
    suspend fun getCount(): HttpResponse<YearStatVO>

    @POST("/jk/record/note")
    fun saveNote(@Body param: NoteParam): Call<HttpResponse<Boolean>>

    @GET("/jk/record/note/{time}")
    fun getNote(@Path("time") time: String): Call<HttpResponse<NoteVO>>

    @GET("/jk/record/top/{time}")
    suspend fun getTop(@Path("time") time: String): HttpResponse<List<RecordTopVO>>
}
