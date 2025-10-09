package ua.gov.diia.core.models.common_compose.org.container

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.AttentionIconMessageMlc
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.checkbox.CheckboxSquareMlc
import ua.gov.diia.core.models.common_compose.mlc.checkbox.TableItemCheckboxMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputDateMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputNumberMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlcV2
import ua.gov.diia.core.models.common_compose.mlc.list.ItemReadMlc
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxCascadeGroupOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxCascadeOrg
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrg
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrgV2
import ua.gov.diia.core.models.common_compose.org.input.question_form.SelectorOrg
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc

@JsonClass(generateAdapter = true)
data class BackgroundWhiteOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "items")
    val items: List<BackgroundWhiteOrgItem>? = null,
) {
    @JsonClass(generateAdapter = true)
    data class BackgroundWhiteOrgItem(
        @Json(name = "checkboxSquareMlc")
        val checkboxSquareMlc: CheckboxSquareMlc? = null,
        @Json(name = "checkboxCascadeOrg")
        val checkboxCascadeOrg: CheckboxCascadeOrg? = null,
        @Json(name = "checkboxCascadeGroupOrg")
        val checkboxCascadeGroupOrg: CheckboxCascadeGroupOrg? = null,
        @Json(name = "attentionIconMessageMlc")
        val attentionIconMessageMlc: AttentionIconMessageMlc? = null,
        @Json(name = "inputDateMlc")
        val inputDateMlc: InputDateMlc? = null,
        @Json(name = "inputPhoneCodeOrg")
        val inputPhoneCodeOrg: InputPhoneCodeOrg? = null,
        @Json(name = "inputPhoneCodeOrgV2")
        val inputPhoneCodeOrgV2: InputPhoneCodeOrgV2? = null,
        @Json(name = "inputTextMlc")
        val inputTextMlc: InputTextMlc? = null,
        @Json(name = "inputTextMlcV2")
        val inputTextMlcV2: InputTextMlcV2? = null,
        @Json(name = "selectorOrg")
        val selectorOrg: SelectorOrg? = null,
        @Json(name = "tableItemCheckboxMlc")
        val tableItemCheckboxMlc: TableItemCheckboxMlc? = null,
        @Json(name = "tableMainHeadingMlc")
        val tableMainHeadingMlc: TableMainHeadingMlc? = null,
        @Json(name = "tableSecondaryHeadingMlc")
        val tableSecondaryHeadingMlc: TableSecondaryHeadingMlc? = null,
        @Json(name = "itemReadMlc")
        val itemReadMlc: ItemReadMlc? = null,
        @Json(name = "headingWithSubtitlesMlc")
        val headingWithSubtitlesMlc: HeadingWithSubtitlesMlc? = null,
        @Json(name = "inputNumberMlc")
        val inputNumberMlc: InputNumberMlc? = null
    )
}