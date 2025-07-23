package com.example.medivol_1.service


import com.example.medivol_1.Constants
import com.example.medivol_1.model.consulta.PageConsultaResponse
import retrofit2.Call

import retrofit2.http.GET

interface ConsultaService {

    @GET(Constants.PATH_ALLCONSULTAS)
    fun getConsultas(): Call<PageConsultaResponse>
}