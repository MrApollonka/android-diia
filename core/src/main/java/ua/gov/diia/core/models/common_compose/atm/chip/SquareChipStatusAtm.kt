package ua.gov.diia.core.models.common_compose.atm.chip


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class SquareChipStatusAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "type")
    val type: ChipType
) : Parcelable {

    enum class ChipType {
        @Json(name = "blue")
        BLUE,

        @Json(name = "gray")
        GRAY
    }

}