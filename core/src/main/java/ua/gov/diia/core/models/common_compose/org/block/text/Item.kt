package ua.gov.diia.core.models.common_compose.org.block.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "textItemHorizontalMlc")
    val textItemHorizontalMlc: TextItemHorizontalMlc? = null,
    @Json(name = "textItemVerticalMlc")
    val textItemVerticalMlc: TextItemVerticalMlc? = null
): Parcelable