package com.example.medivol_1

import TokenManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medivol_1.api.ApiLogin
import com.example.medivol_1.model.LoginRequest
import com.example.medivol_1.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {



    //definindo variables
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        //capturando campos del formulario login
        loginEditText = findViewById(R.id.etLogin)
        passwordEditText = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)

        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val contrasenia = passwordEditText.text.toString()
            val loginRequest = LoginRequest(login, contrasenia)

            ApiLogin.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token

                        if (token != null) {
                            // Usa el TokenManager para guardar el token y el rol
                            TokenManager.saveToken(this@LoginActivity, token)
                            // Luego navegas a la actividad principal
                            // Guardar token en SharedPreferences
                            val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                            sharedPref.edit().putString("token", token).apply()
                            Log.e("TokenGuardado", "objeto donde se gaurda el token: $sharedPref")
                            // Navegar a otra Activity
                            val intent = Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Token no recibido.", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@LoginActivity, "Login incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }


        // tool bar, ecencial para mantener el estilo del hora en la parte superior de la app
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}