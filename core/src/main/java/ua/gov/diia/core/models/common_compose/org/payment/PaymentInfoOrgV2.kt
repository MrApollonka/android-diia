package ua.gov.diia.core.models.common_compose.org.payment


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalLargeMlc

@JsonClass(generateAdapter = true)
data class PaymentInfoOrgV2(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "tableItemHorizontalLargeMlc")
    val tableItemHorizontalLargeMlc: TableItemHorizontalLargeMlc?,
    @Json(name = "title")
    val title: String?
)