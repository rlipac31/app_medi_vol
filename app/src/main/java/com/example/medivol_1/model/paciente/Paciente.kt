package com.example.medivol_1.model.paciente

import com.example.medivol_1.model.Direccion
import com.google.gson.annotations.SerializedName

data class Paciente(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("documento_identidad") val documento_identidad: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("direccion") val direccion: Direccion // Ahora es un objeto Direccion

)