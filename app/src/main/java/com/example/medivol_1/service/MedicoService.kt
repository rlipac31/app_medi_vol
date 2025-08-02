package com.example.medivol_1.service

import com.example.medivol_1.Constants

import com.example.medivol_1.model.medico.Medico
import com.example.medivol_1.model.medico.MedicoUpdateRequest
import com.example.medivol_1.model.medico.PagedMedicosResponse
import com.example.medivol_1.model.medico.RegistroMedicoRequest
import com.example.medivol_1.model.medico.RegistroMedicoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedicoService {

    @GET(Constants.PATH_MEDICOS)
    suspend fun getMedicos(): PagedMedicosResponse

    @POST(Constants.PATH_MEDICOS)
   fun getRegistroMedico(@Body request: RegistroMedicoRequest): Call<RegistroMedicoResponse>

    @GET(Constants.PATH_MEDICO_ID)
    fun getDetalleMedico(
        @Path("idMedico") idMedico: Long
    ): Call<Medico>

    @PUT("medicos/{idMedico}")
    fun updateMedico(
        @Path("idMedico") idMedico: Long,
        @Body request: MedicoUpdateRequest
    ): Call<Medico>

    @DELETE("medicos/{idMedico}")
    fun deleteMedico(
        @Path("idMedico") idMedico: Long
    ):  Call<Unit> // Cambiado a Unit


}