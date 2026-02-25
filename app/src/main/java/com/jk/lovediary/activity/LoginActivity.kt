package com.jk.lovediary.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.jk.lovediary.R
import com.jk.lovediary.data.CheckInStore
import com.jk.lovediary.model.param.LoginParam
import com.jk.lovediary.model.response.HttpResponse
import com.jk.lovediary.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var store = CheckInStore(this)

        val usernameEditText = findViewById<EditText>(R.id.phoneEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)



        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = LoginParam(
                userName = username,
                password = password
            )

            val call = RetrofitClient.instance.login(user)
            call.enqueue(object : Callback<HttpResponse<String>> {
                override fun onResponse(call: Call<HttpResponse<String>>, response: Response<HttpResponse<String>>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.code == 200) {
                            Toast.makeText(this@LoginActivity, "登录成功！", Toast.LENGTH_SHORT).show()

                            var token = result?.data
                            val resultIntent = Intent()
                            resultIntent.putExtra("token", token)
                            setResult(RESULT_OK, resultIntent)

                            store.setLoginToken(token.toString())

                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "登录失败: ${result?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "请求失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<HttpResponse<String>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}