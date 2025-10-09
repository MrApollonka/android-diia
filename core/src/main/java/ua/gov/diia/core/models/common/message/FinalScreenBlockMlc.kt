package ua.gov.diia.core.models.common.message


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.ExtraLargeIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class FinalScreenBlockMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "extraLargeIconAtm")
    val extraLargeIconAtm: ExtraLargeIconAtm?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "title")
    val title: String?
): Parcelable