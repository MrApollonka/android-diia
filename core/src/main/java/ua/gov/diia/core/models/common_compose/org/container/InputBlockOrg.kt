package ua.gov.diia.core.models.common_compose.org.container

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.AttentionIconMessageMlc
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlcV2
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrgV2
import ua.gov.diia.core.models.common_compose.org.input.SelectorOrgV2
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc

@JsonClass(generateAdapter = true)
data class InputBlockOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "tableMainHeadingMlc")
    val tableMainHeadingMlc: TableMainHeadingMlc? = null,
    @Json(name = "tableSecondaryHeadingMlc")
    val tableSecondaryHeadingMlc: TableSecondaryHeadingMlc? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
    @Json(name = "attentionIconMessageMlc")
    val attentionIconMessageMlc: AttentionIconMessageMlc? = null
) {

    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "inputPhoneCodeOrgV2")
        val inputPhoneCodeOrgV2: InputPhoneCodeOrgV2? = null,
        @Json(name = "inputTextMlcV2")
        val inputTextMlcV2: InputTextMlcV2? = null,
        @Json(name = "selectorOrgV2")
        val selectorOrgV2: SelectorOrgV2? = null,
    )
}