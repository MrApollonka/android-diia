package ua.gov.diia.core.models.common_compose.table.tableBlockOrg

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.AttentionIconMessageMlc
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelPlaneMlc
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockOrgV2(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode? = null,
    @Json(name = "chipStatusAtm")
    val chipStatusAtm: ChipStatusAtm? = null,
    @Json(name = "tableMainHeadingMlc")
    val tableMainHeadingMlc: TableMainHeadingMlc? = null,
    @Json(name = "tableSecondaryHeadingMlc")
    val tableSecondaryHeadingMlc: TableSecondaryHeadingMlc? = null,
    @Json(name = "items")
    val items: List<TableBlockOrgV2Item>? = null,
    @Json(name = "attentionIconMessageMlc")
    val attentionIconMessageMlc: AttentionIconMessageMlc? = null,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockOrgV2Item(
    @Json(name = "smallEmojiPanelMlc")
    val smallEmojiPanelMlc: SmallEmojiPanelMlc? = null,
    @Json(name = "smallEmojiPanelPlaneMlc")
    val smallEmojiPanelPlaneMlc: SmallEmojiPanelPlaneMlc? = null,
    @Json(name = "tableItemPrimaryMlc")
    val tableItemPrimaryMlc: TableItemPrimaryMlc? = null,
    @Json(name = "tableItemHorizontalMlc")
    val tableItemHorizontalMlc: TableItemHorizontalMlc? = null,
    @Json(name = "tableItemVerticalMlc")
    val tableItemVerticalMlc: TableItemVerticalMlc? = null,
) : Parcelable