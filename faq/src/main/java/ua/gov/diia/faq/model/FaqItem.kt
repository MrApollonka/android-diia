package ua.gov.diia.faq.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class FaqItem(
    @Json(name = "question")
    val question: String,
    @Json(name = "searchText")
    val searchText: String,
    @Json(name = "answer")
    val answer: String,
    @Json(name = "parameters")
    val parameters: List<TextParameter>
)