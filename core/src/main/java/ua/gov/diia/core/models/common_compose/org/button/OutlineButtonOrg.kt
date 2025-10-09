package ua.gov.diia.core.models.common_compose.org.button


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.button.OutlineButtonMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class OutlineButtonOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode? = null,
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "outlineButtonMlc")
        val outlineButtonMlc: OutlineButtonMlc?
    ) : Parcelable
}