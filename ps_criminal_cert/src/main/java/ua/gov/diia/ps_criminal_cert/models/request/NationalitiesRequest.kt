package ua.gov.diia.ps_criminal_cert.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NationalitiesRequest(
    @Json(name = "citizenship")
    val citizenship: List<Citizenship>?,
    @Json(name = "citizenshipFirst")
    val citizenshipFirst: String?
)