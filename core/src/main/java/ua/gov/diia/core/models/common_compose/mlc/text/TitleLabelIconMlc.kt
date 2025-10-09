package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.LogoAtm

@JsonClass(generateAdapter = true)
data class TitleLabelIconMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "logoAtm")
    val logoAtm: LogoAtm?
)