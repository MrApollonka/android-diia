package ua.gov.diia.core.models.common.message


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class StubInfoMessageMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconAtm")
    val iconAtm: IconAtm?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
) : Parcelable