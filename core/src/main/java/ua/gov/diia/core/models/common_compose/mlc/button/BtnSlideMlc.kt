package ua.gov.diia.core.models.common_compose.mlc.button

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class BtnSlideMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "icon")
    val icon: IconAtm?,
    @Json(name = "state")
    val state: String?, //"enum['enabled ', 'disabled']"
    @Json(name = "action")
    val action: Action?
)
