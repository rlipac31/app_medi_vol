package com.example.medivol_1.model
import com.google.gson.annotations.SerializedName
data class Direccion(
    @SerializedName("calle") val calle: String,
    @SerializedName("numero") val numero: String,
    @SerializedName("complemento") val complemento: String?, // Puede ser nulo o vacío
    @SerializedName("barrio") val barrio: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("codigo_postal") val codigo_postal: String
)
