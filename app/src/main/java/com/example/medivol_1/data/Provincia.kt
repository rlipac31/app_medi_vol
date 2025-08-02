package com.example.medivol_1.data

import com.google.gson.annotations.SerializedName

data class Provincia(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("department_id") val department_id: String
)
