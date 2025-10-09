package ua.gov.diia.core.models.common_compose.general

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.FinalScreenBlockMlc
import ua.gov.diia.core.models.common_compose.org.payment.PaymentInfoOrgV2

@JsonClass(generateAdapter = true)
data class CenteredBody(
    @Json(name = "finalScreenBlockMlc")
    val finalScreenBlockMlc: FinalScreenBlockMlc? = null,
    @Json(name = "paymentInfoOrgV2")
    val paymentInfoOrgV2: PaymentInfoOrgV2? = null,
)