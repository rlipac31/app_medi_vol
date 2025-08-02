package com.example.medivol_1.model.medico
import android.os.Parcelable
import com.example.medivol_1.model.Direccion
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Medico(
@SerializedName("id") val id: Long,
@SerializedName("nombre") val nombre: String,
@SerializedName("email") val email: String,
@SerializedName("documento") val documento: String,
@SerializedName("especialidad") val especialidad: String,
@SerializedName("telefono") val telefono: String,
@SerializedName("direccion") val direccion:Direccion // Ahora es un objeto Direccion

): Parcelable