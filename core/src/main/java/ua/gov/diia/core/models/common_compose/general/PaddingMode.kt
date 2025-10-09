package ua.gov.diia.core.models.common_compose.general

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PaddingMode(
    @Json(name = "side")
    val side: ElementPaddingMode?,
    @Json(name = "top")
    val top: ElementPaddingMode?
) : Parcelable

enum class ElementPaddingMode {
    @Json(name = "none")
    NONE,

    @Json(name = "medium")
    MEDIUM,

    @Json(name = "large")
    LARGE
}