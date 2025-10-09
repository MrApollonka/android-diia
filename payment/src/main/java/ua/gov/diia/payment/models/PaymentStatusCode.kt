package ua.gov.diia.payment.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentStatusCode(
    @Json(name = "statusCode")
    val statusCode: String?
)