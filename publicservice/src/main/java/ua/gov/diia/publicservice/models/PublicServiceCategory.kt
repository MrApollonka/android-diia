package ua.gov.diia.publicservice.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PublicServiceCategory(
    @Json(name = "code")
    val code: String,
    @Json(name = "sortOrder")
    val sortOrder: Int,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: CategoryStatus,
    @Json(name = "visibleSearch")
    val visibleSearch: Boolean,
    @Json(name = "publicServices")
    val publicServices: List<PublicService>,
    @Json(name = "tabCode")
    val tabCode: String?,
    @Json(name = "tabCodes")
    val tabCodes: List<String>?,
    @Json(name = "chips")
    val chips: List<PublicServiceChip>?
) : Parcelable {

    val isSingleServiceCategory: Boolean
        get() = publicServices.size == 1

    val singleService: PublicService?
        get() = publicServices.firstOrNull()

    val hasServices: Boolean
        get() = publicServices.isNotEmpty()

}