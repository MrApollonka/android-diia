package ua.gov.diia.core.models.common_compose.mlc.list


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class ItemReadMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconRight")
    val iconRight: SmallIconAtm?,
    @Json(name = "label?")
    val label: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "value")
    val value: String?
): Parcelable