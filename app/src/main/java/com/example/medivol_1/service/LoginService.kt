package com.example.medivol_1.service

import com.example.medivol_1.Constants
import com.example.medivol_1.model.LoginRequest
import com.example.medivol_1.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @POST(Constants.PATH_LOGIN)
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}