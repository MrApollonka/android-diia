package ua.gov.diia.publicservice.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PublicServiceChip(
    @Json(name = "tab")
    val tab: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "type")
    val type: Type
) : Parcelable {

    enum class Type {
        @Json(name = "blue")
        BLUE,

        @Json(name = "gray")
        GRAY
    }

}