package com.example.medivol_1.service

import com.example.medivol_1.Constants
import com.example.medivol_1.model.paciente.PagePacienteResponse
import retrofit2.http.GET

interface PacienteService {

    @GET(Constants.PATH_ALLPACIENTES)
   suspend  fun getPacientes(): PagePacienteResponse
}