package ua.gov.diia.core.models.common_compose.mlc.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryAdditionalAtm
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@JsonClass(generateAdapter = true)
data class AlertCardMlcV2(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "iconAtm")
    val iconAtm: IconAtm,
    @Json(name = "label")
    val label: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "btnPrimaryAdditionalAtm")
    val btnPrimaryAdditionalAtm: BtnPrimaryAdditionalAtm?,
)
