package ua.gov.diia.ps_criminal_cert.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FullNameBefore(
    @Json(name = "firstNameBefore")
    val firstNameBefore: String?,
    @Json(name = "lastNameBefore")
    val lastNameBefore: String?,
    @Json(name = "middleNameBefore")
    val middleNameBefore: String?
)