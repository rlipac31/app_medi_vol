package com.example.medivol_1.model.medico
import com.google.gson.annotations.SerializedName
import com.example.medivol_1.model.Direccion // Importa el modelo Direccion
data class Medico(

@SerializedName("id") val id: Long,
@SerializedName("nombre") val nombre: String,
@SerializedName("email") val email: String,
@SerializedName("documento") val documento: String,
@SerializedName("especialidad") val especialidad: String,
@SerializedName("telefono") val telefono: String,
@SerializedName("direccion") val direccion: Direccion // Ahora es un objeto Direccion

)