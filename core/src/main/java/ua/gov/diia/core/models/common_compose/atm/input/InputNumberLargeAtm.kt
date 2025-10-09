package ua.gov.diia.core.models.common_compose.atm.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class InputNumberLargeAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "inputCode")
    val inputCode: String?,
)