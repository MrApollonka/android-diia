package ua.gov.diia.core.models.common_compose.org.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc

@JsonClass(generateAdapter = true)
data class SearchScreenData(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "searchScreenTitle")
    val searchScreenTitle: String? = null,
    @Json(name = "searchPlaceholder")
    val searchPlaceholder: String,
    @Json(name = "items")
    val items: List<SelectorListWidgetOrgItem>
)

@JsonClass(generateAdapter = true)
data class SelectorListWidgetOrgItem(
    @Json(name = "listItemMlc")
    val listItemMlc: ListItemMlc?
)