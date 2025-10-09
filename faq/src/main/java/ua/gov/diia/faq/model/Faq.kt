package ua.gov.diia.faq.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Faq(
    @Json(name = "expirationDate")
    val expirationDate: String?,
    @Json(name = "categories")
    val categories: List<CategoryItem>,
    @Json(name = "categoriesGroups")
    val categoriesGroups: List<CategoryGroup>
)