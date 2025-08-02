package com.example.medivol_1.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Direccion(
    @SerializedName("calle") val calle: String,
    @SerializedName("numero") val numero: String,
    @SerializedName("complemento") val complemento: String?, // Puede ser nulo o vacío
    @SerializedName("barrio") val distrito: String?,
    @SerializedName("ciudad") val provincia: String?,
    @SerializedName("estado") val departamento: String?,
    @SerializedName("codigo_postal") val codigo_postal: String?

): Parcelable
