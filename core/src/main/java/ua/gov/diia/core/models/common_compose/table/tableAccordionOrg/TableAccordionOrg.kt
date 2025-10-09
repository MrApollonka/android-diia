package ua.gov.diia.core.models.common_compose.table.tableAccordionOrg

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.AttentionIconMessageMlc
import ua.gov.diia.core.models.common_compose.org.accordion.AccordionOrg
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class TableAccordionOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "tableMainHeadingMlc")
    val tableMainHeadingMlc: TableMainHeadingMlc? = null,
    @Json(name = "items")
    val items: List<TableAccordionItem>? = null,
    @Json(name = "attentionIconMessageMlc")
    val attentionIconMessageMlc: AttentionIconMessageMlc? = null,
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class TableAccordionItem(
    @Json(name = "accordionOrg")
    val accordionOrg: AccordionOrg? = null
): Parcelable