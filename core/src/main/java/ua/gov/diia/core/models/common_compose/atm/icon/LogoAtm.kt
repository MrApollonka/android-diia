package ua.gov.diia.core.models.common_compose.atm.icon

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class LogoAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "logo")
    val logo: String,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "accessibilityDescription")
    val accessibilityDescription: String?,
    @Json(name = "action")
    val action: Action?
)