package ua.gov.diia.core.models.common_compose.mlc.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeAdditionalAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class PaginationMessageMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "btnStrokeAdditionalAtm")
    val btnStrokeAdditionalAtm: BtnStrokeAdditionalAtm?
): Parcelable
