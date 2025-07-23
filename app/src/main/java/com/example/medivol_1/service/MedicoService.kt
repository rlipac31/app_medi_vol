package com.example.medivol_1.service

import com.example.medivol_1.Constants
import com.example.medivol_1.model.medico.Medico
import com.example.medivol_1.model.medico.PagedMedicosResponse
import retrofit2.http.GET

interface MedicoService {

    @GET(Constants.PATH_ALLMEDICOS)
    suspend fun getMedicos(): PagedMedicosResponse
}