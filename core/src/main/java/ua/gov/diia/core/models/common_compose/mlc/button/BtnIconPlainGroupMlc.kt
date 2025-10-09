package ua.gov.diia.core.models.common_compose.mlc.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class BtnIconPlainGroupMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "items")
    val items: List<Item>?,

) : Parcelable {

    @Parcelize
    data class Item(
        @Json(name = "btnPlainIconAtm")
        val btnPlainIconAtm: BtnPlainIconAtm,
    ) : Parcelable

}