package ua.gov.diia.core.models.common_compose.mlc.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@JsonClass(generateAdapter = true)
data class InputNumberMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "mask")
    val mask: String?,
    @Json(name = "value")
    val value: Long?,
    @Json(name = "minValue")
    val minValue: Long?,
    @Json(name = "maxValue")
    val maxValue: Long?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "errorMessage")
    val errorMessage: String?,
    @Json(name = "iconRight")
    val iconRight: SmallIconAtm?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "isDisabled")
    val isDisabled: Boolean?
)