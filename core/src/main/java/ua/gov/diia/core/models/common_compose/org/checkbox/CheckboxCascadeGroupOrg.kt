package ua.gov.diia.core.models.common_compose.org.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CheckboxCascadeGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "minMandatorySelectedItems")
    val minMandatorySelectedItems: Int?
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "checkboxCascadeOrg")
        val checkboxCascadeOrg: CheckboxCascadeOrg?
    ) : Parcelable
}