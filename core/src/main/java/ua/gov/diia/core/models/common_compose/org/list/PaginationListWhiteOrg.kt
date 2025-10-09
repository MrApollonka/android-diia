package ua.gov.diia.core.models.common_compose.org.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@JsonClass(generateAdapter = true)
data class PaginationListWhiteOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "showDivider")
    val showDivider: Boolean? = null,
    @Json(name = "items")
    val items: List<PaginationItem>,
    @Json(name = "limit")
    val limit: Int?
)