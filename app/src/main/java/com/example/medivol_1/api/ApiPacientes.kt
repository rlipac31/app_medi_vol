package com.example.medivol_1.api

import com.example.medivol_1.Constants
import com.example.medivol_1.service.LoginService
import com.example.medivol_1.service.PacienteService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiPacientes {

    private  lateinit var pacienteService: PacienteService

    val instance: PacienteService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PacienteService::class.java)
    }
}