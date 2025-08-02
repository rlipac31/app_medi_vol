package com.example.medivol_1.model.medico

import com.example.medivol_1.model.Direccion

data class RegistroMedicoResponse(
    val id:Long,
    val nombre: String,
    val email:String,
    val  especialidad:Especialidad,
    val telefono: String,
    val documento:  String,
    val direccion: Direccion
)
