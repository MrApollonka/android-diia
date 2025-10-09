package ua.gov.diia.address_search.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchData(
    @Json(name = "title")
    val title: String? = "Пошук",
    @Json(name = "searchPlaceholder")
    val searchPlaceholder: String? = "Пошук"
)