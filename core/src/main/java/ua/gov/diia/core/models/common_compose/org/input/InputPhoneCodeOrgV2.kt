package ua.gov.diia.core.models.common_compose.org.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlcV2
import ua.gov.diia.core.models.common_compose.mlc.input.Validation

@JsonClass(generateAdapter = true)
data class InputPhoneCodeOrgV2(
    @Json(name = "codeValueId")
    val codeValueId: String?,
    @Json(name = "codeValueIsEditable")
    val codeValueIsEditable: Boolean?,
    @Json(name = "codes")
    val codes: List<Code>?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "inputPhoneMlcV2")
    val inputPhoneMlcV2: InputPhoneMlcV2?,
    @Json(name = "isDisable")
    val isDisable: Boolean?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?
) {
    @JsonClass(generateAdapter = true)
    data class Code(
        @Json(name = "id")
        val id: String,
        @Json(name = "maskCode")
        val maskCode: String?,
        @Json(name = "placeholder")
        val placeholder: String?,
        @Json(name = "label")
        val label: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "value")
        val value: String,
        @Json(name = "icon")
        val icon: String,
        @Json(name = "validation")
        val validation: List<Validation>?
    )
}