package ua.gov.diia.core.models.common_compose.org.radioBtn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.button.RadioBtnMlcV2

@JsonClass(generateAdapter = true)
data class RadioBtnGroupOrgV2(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "inputCode")
    val inputCode: String? = null,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "items")
    val items: List<RadioBtnGroupOrgV2Item>,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
)

@JsonClass(generateAdapter = true)
data class RadioBtnGroupOrgV2Item(
    @Json(name = "radioBtnMlcV2")
    val radioBtnMlcV2: RadioBtnMlcV2?,
)