package ua.gov.diia.core.models.common_compose.mlc.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.MediumIconAtm
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class BankingCardMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "gradient")
    val gradient: String? = null,
    @Json(name = "paymentSystemLogo")
    val paymentSystemLogo: String?,
    @Json(name = "cardNumMask")
    val cardNumMask: String?,
    @Json(name = "expirationDate")
    val expirationDate: String?,
    @Json(name = "logos")
    val logos: List<BankingCardLogoItem>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "action")
    val action: Action?
) {
    @JsonClass(generateAdapter = true)
    data class BankingCardLogoItem(
        @Json(name = "mediumIconAtm")
        val mediumIconAtm: MediumIconAtm? = null,
    )
}