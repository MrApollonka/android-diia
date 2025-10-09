package ua.gov.diia.ps_criminal_cert.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Citizenship(
    @Json(name = "citizenshipCountry")
    val citizenshipCountry: String?
)