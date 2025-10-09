package ua.gov.diia.core.models.common_compose.org.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.checkbox.TableItemCheckboxMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class CheckboxCascadeOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "tableItemCheckboxMlc")
    val tableItemCheckboxMlc: TableItemCheckboxMlc?,
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
    @Json(name = "minMandatorySelectedItems")
    val minMandatorySelectedItems: Int?,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "tableItemCheckboxMlc")
        val tableItemCheckboxMlc: TableItemCheckboxMlc?
    ) : Parcelable
}