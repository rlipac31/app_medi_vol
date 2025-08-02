package com.example.medivol_1.model.medico

import com.example.medivol_1.model.Direccion
import com.google.gson.annotations.SerializedName


data class RegistroMedicoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email:String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("documento") val documento:  String,
    @SerializedName("direccion") val direccion: Direccion
)
