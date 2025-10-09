package ua.gov.diia.core.models.common_compose.mlc.input


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class InputTextMlcV2(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "isDisable")
    val isDisable: Boolean?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "validation")
    val validation: List<Validation>?,
    @Json(name = "value")
    val value: String?
) : Parcelable