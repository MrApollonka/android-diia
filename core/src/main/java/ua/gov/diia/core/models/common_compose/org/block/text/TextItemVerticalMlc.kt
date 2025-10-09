package ua.gov.diia.core.models.common_compose.org.block.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class TextItemVerticalMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconRight")
    val iconRight: SmallIconAtm?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "value")
    val value: String?
): Parcelable