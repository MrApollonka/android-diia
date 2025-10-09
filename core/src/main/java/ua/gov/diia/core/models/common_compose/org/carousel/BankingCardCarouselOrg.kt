package ua.gov.diia.core.models.common_compose.org.carousel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.indicators.DotNavigationAtm
import ua.gov.diia.core.models.common_compose.mlc.card.BankingCardMlc

@JsonClass(generateAdapter = true)
data class BankingCardCarouselOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "dotNavigationAtm")
    val dotNavigationAtm: DotNavigationAtm? = null,
    @Json(name = "items")
    val items: List<BankingCardItem>
){
    @JsonClass(generateAdapter = true)
    data class BankingCardItem(
        @Json(name = "bankingCardMlc")
        val bankingCardMlc: BankingCardMlc? = null,
    )
}

