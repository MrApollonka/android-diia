package ua.gov.diia.core.models.common_compose.org.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.org.container.BackgroundWhiteOrg

@JsonClass(generateAdapter = true)
data class RecursiveContainerOrgItem(
    @Json(name = "backgroundWhiteOrg")
    val backgroundWhiteOrg: BackgroundWhiteOrg?
)