package com.example.medivol_1.data

import com.google.gson.annotations.SerializedName

data class Departamento(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)
