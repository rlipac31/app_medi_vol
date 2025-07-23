package com.example.medivol_1.api

import com.example.medivol_1.Constants
import com.example.medivol_1.service.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiLogin {
    private  lateinit var loginService: LoginService
 //   private const val BASE_URL = "http://tubackend.com/api/"

    val instance: LoginService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LoginService::class.java)
    }
}
