package com.jk.lovediary.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.jk.lovediary.R
import com.jk.lovediary.model.param.UserCreateParam
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val usernameRegister = findViewById<EditText>(R.id.usernameRegister)
        val phoneRegister = findViewById<EditText>(R.id.phoneRegister)
        val passwordRegister = findViewById<EditText>(R.id.passwordRegister)
        val loginButton = findViewById<Button>(R.id.register)

        loginButton.setOnClickListener {
            val username = usernameRegister.text.toString()
            val phone = phoneRegister.text.toString()
            val password = passwordRegister.text.toString()

            val user = UserCreateParam(
                userName = username,
                phoneNum = phone,
                password = password
            )

            val call = RetrofitClient.instance.register(user)
            call.enqueue(object : Callback<HttpResponse<Boolean>> {
                override fun onResponse(call: Call<HttpResponse<Boolean>>, response: Response<HttpResponse<Boolean>>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.code == 200) {
                            Toast.makeText(this@RegisterActivity, "注册成功！", Toast.LENGTH_SHORT).show()

                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "注册失败: ${result?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "请求失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<HttpResponse<Boolean>>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}