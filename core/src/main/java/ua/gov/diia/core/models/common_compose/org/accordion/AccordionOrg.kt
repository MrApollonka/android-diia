package ua.gov.diia.core.models.common_compose.org.accordion

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class AccordionOrg(
    @Json(name = "componentId")
    val componentId: String,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode? = null,
    @Json(name = "heading")
    val heading: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "states")
    val states: AccordionState? = null,
    @Json(name = "expandedContent")
    val expandedContent: ExpandedContent? = null,
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class AccordionState(
    @Json(name = "isExpanded")
    val isExpanded: Boolean? = null,
    @Json(name = "expandedIcon")
    val expandedIcon: SmallIconAtm? = null,
    @Json(name = "collapsedIcon")
    val collapsedIcon: SmallIconAtm? = null
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ExpandedContent(
    @Json(name = "items")
    val items: List<AccordionContentItem>? = null,
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class AccordionContentItem(
    @Json(name = "tableItemVerticalMlc")
    val tableItemVerticalMlc: TableItemVerticalMlc? = null,
    @Json(name = "tableItemHorizontalMlc")
    val tableItemHorizontalMlc: TableItemHorizontalMlc? = null,
): Parcelable