package ua.gov.diia.core.models.common_compose.mlc.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class RadioBtnMlcV2(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "iconLeft")
    val iconLeft: SmallIconAtm?,
    @Json(name = "iconRight")
    val iconRight: SmallIconAtm?,
    @Json(name = "iconBigRight")
    val iconBigRight: SmallIconAtm?,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "isSelected")
    val isSelected: Boolean?,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
    @Json(name = "dataJson")
    val dataJson: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
): Parcelable