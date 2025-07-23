package com.example.medivol_1.model.consulta

import com.example.medivol_1.model.Direccion
import com.google.gson.annotations.SerializedName

data class Consulta(
    @SerializedName("id") val id: Long,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("nombre_medico") val nombre: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("documento_medico") val documento_medico: String,
    @SerializedName("nombre_paciente") val nombre_paciente: String,
    @SerializedName("email_paciente") val email_paciente: String,
    @SerializedName("phone_paciente") val phone_paciente: String,
    @SerializedName("documento_paciente") val documento_paciente: String,
    @SerializedName("direccion") val direccion: Direccion // Ahora es un objeto Direccion
)
