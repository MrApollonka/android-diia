package ua.gov.diia.core.models.common_compose.org.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SelectedCheckboxCascadeOrg(
    @Json(name = "inputCode")
    val inputCode: String,
    @Json(name = "values")
    val values: List<String>
) : Parcelable