package ua.gov.diia.ps_criminal_cert.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepeatedDeliveryConfirmationRequest(
    @Json(name = "addressId")
    val addressId: String?,
    @Json(name = "applicationId")
    val applicationId: String?,
    @Json(name = "emailAddress")
    val emailAddress: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "typeDelivery")
    val typeDelivery: String?
)