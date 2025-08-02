package com.example.medivol_1.data

import com.google.gson.annotations.SerializedName


data class Distrito(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("province_id") val province_id: String,
    @SerializedName("department_id") val department_id: String
)
