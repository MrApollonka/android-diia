package ua.gov.diia.core.models.common_compose.org.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@JsonClass(generateAdapter = true)
data class SelectorOrgV2(
    @Json(name = "componentId")
    val componentId: String,
    @Json(name = "id")
    val id: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "label")
    val label: String,
    @Json(name = "placeholder")
    val placeholder: String,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "valueId")
    val valueId: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
    @Json(name = "searchScreenData")
    val searchScreenData: SearchScreenData?,
    @Json(name = "action")
    val action: Action?,
)
