package ua.gov.diia.core.models.common_compose.org.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnWhiteLargeIconAtm
import ua.gov.diia.core.models.common_compose.org.container.BackgroundWhiteOrg

@JsonClass(generateAdapter = true)
data class RecursiveContainerOrg(
    @Json(name = "btnWhiteLargeIconAtm")
    val btnWhiteLargeIconAtm: BtnWhiteLargeIconAtm?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "items")
    val items: List<RecursiveContainerOrgItem>?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "maxNumber")
    val maxNumber: Int?,
    @Json(name = "template")
    val template: Template?
) {
    @JsonClass(generateAdapter = true)
    data class Template(
        @Json(name = "backgroundWhiteOrg")
        val backgroundWhiteOrg: BackgroundWhiteOrg?
    )
}