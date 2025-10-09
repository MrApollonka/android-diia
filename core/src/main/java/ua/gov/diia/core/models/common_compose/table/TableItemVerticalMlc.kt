package ua.gov.diia.core.models.common_compose.table

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode

@Parcelize
@JsonClass(generateAdapter = true)
data class TableItemVerticalMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode? = null,
    @Json(name = "icon")
    val icon: IconAtm? = null,
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "secondaryLabel")
    val secondaryLabel: String? = null,
    @Json(name = "secondaryValue")
    val secondaryValue: String? = null,
    @Json(name = "supportingValue")
    val supportingValue: String? = null,
    @Json(name = "pointSupportingValue")
    val pointSupportingValue: String? = null,
    @Json(name = "value")
    val value: String? = null,
    @Json(name = "valueIcons")
    val valueIcons: List<ValueIcon>? = null,
    @Json(name = "valueImage")
    val valueImage: String? = null,
    @Json(name = "valueImages")
    val valueImages: List<String>? = null,
    @Json(name = "valueParameters")
    val valueParameters: List<TextParameter>? = null,
): Parcelable