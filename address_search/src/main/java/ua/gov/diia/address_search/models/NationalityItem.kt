package ua.gov.diia.address_search.models

import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.search.models.SearchableItem

@Parcelize
@JsonClass(generateAdapter = true)
data class NationalityItem(
    val componentId : String?,
    val id: String?,
    val label : String,
    val containerId: String?
) : SearchableItem {
    override fun getDisplayTitle(): String {
        return label
    }

    override fun getQueryString(): String {
        return label
    }

    override fun isDisabled(): Boolean = false

}