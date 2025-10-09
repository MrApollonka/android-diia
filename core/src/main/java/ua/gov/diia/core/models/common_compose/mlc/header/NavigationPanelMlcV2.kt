package ua.gov.diia.core.models.common_compose.mlc.header


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.MiddleIconAtm

@JsonClass(generateAdapter = true)
data class NavigationPanelMlcV2(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconLeft")
    val iconLeft: MiddleIconAtm?,
    @Json(name = "isClosed")
    val isClosed:  Boolean?,
    @Json(name = "title")
    val title: String?
)