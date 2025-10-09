package ua.gov.diia.core.models.common_compose.mlc.button


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.general.Action


@Parcelize
@JsonClass(generateAdapter = true)
data class OutlineButtonMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconLeft")
    val iconLeft: SmallIconAtm?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "title")
    val title: String?
): Parcelable