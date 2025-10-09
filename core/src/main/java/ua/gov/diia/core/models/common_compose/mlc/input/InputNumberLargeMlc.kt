package ua.gov.diia.core.models.common_compose.mlc.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.input.InputNumberLargeAtm

@JsonClass(generateAdapter = true)
data class InputNumberLargeMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "mandatoryCounter")
    val mandatoryCounter: Int?,
    @Json(name = "count")
    val count: Int?,
    @Json(name = "items")
    val items: List<Item>?,
)

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "inputNumberLargeAtm")
    val inputNumberLargeAtm: InputNumberLargeAtm? = null,
)