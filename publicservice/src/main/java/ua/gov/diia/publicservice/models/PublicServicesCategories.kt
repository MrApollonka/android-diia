package ua.gov.diia.publicservice.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.org.carousel.HalvedCardCarouselOrg

@JsonClass(generateAdapter = true)
data class PublicServicesCategories(
    @Json(name = "publicServicesCategories")
    val categories: List<PublicServiceCategory>,
    @Json(name = "tabs")
    val tabs: List<PublicServiceTab>,
    @Json(name = "additionalElements")
    val additionalElements: List<AdditionalElement>?
) {

    @JsonClass(generateAdapter = true)
    data class AdditionalElement(
        @Json(name = "tabCodes")
        val tabCodes: List<String>?,
        @Json(name = "halvedCardCarouselOrg")
        val halvedCardCarouselOrg: HalvedCardCarouselOrg?
    )

}