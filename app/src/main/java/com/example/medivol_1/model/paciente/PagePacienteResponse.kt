package com.example.medivol_1.model.paciente



import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.example.medivol_1.model.paciente.Paciente

data class PagePacienteResponse(
    @SerializedName("content") val content: List<Paciente>, // Esta es la clave principal para tu lista de médicos
    @SerializedName("pageable") val pageable: Pageable? = null, // Puedes crear un data class para Pageable si lo necesitas
    @SerializedName("last") val last: Boolean? = null,
    @SerializedName("totalPages") val totalPages: Int? = null,
    @SerializedName("totalElements") val totalElements: Int? = null,
    @SerializedName("first") val first: Boolean? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("number") val number: Int? = null,
    @SerializedName("sort") val sort: Sort? = null, // Puedes crear un data class para Sort si lo necesitas
    @SerializedName("numberOfElements") val numberOfElements: Int? = null,
    @SerializedName("empty") val empty: Boolean? = null
)

// Opcional: Si necesitas acceder a los detalles de paginación o ordenación,
// puedes definir estas data classes. Si no, puedes dejarlos como nulos o simplemente no incluirlos.
data class Pageable(
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("offset") val offset: Int,
    @SerializedName("paged") val paged: Boolean,
    @SerializedName("unpaged") val unpaged: Boolean
)

data class Sort(
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("sorted") val sorted: Boolean,
    @SerializedName("unsorted") val unsorted: Boolean
)
