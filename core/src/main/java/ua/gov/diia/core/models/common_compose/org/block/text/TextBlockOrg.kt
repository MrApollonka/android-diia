package ua.gov.diia.core.models.common_compose.org.block.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.chip.SquareChipStatusAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class TextBlockOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "listItems")
    val listItems: List<ListItem>?,
    @Json(name = "squareChipStatusAtm")
    val squareChipStatusAtm: SquareChipStatusAtm?,
): Parcelable