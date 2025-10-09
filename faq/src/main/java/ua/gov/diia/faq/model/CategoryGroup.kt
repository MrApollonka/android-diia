package ua.gov.diia.faq.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryGroup(
    @Json(name = "code")
    val code: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "categoriesGroups")
    val categoriesGroups: List<String>?,
    @Json(name = "categories")
    val categories: List<String>?
)