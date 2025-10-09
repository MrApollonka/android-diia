package ua.gov.diia.payment.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class PaymentDataJson(
    @Json(name = "dataJson")
    val dataJson: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)