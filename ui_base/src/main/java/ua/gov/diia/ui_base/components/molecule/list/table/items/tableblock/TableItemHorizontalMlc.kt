package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.model.TableItemLabelAlignment
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.subatomic.icon.SingIconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackAlpha54
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.extensions.language.detectLanguageCode

@Composable
fun TableItemHorizontalMlc(
    modifier: Modifier = Modifier,
    data: TableItemHorizontalMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val rootAlignment =
        if (data.secondaryTitle == null && data.secondaryValue == null && data.iconRight != null && data.valueAsBase64String == null) {
            Alignment.CenterVertically
        } else {
            Alignment.Top
        }
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 24.dp)
            .semantics {
                testTag = data.componentId
            },
        verticalAlignment = rootAlignment
    ) {
        Row(modifier = Modifier.weight(1f)) {
            data.supportText?.let {
                Text(
                    modifier = Modifier
                        .width(30.dp),
                    text = data.supportText,
                    style = DiiaTextStyle.t3TextBody,
                    color = Black,
                    textAlign = TextAlign.Right,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                val title = data.title?.asString()
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = DiiaTextStyle.t3TextBody,
                        color = Black
                    )
                }

                val secondaryTitle = data.secondaryTitle?.asString()
                if (!secondaryTitle.isNullOrEmpty()) {
                    Text(
                        text = buildAnnotatedString {
                            val languageCode = secondaryTitle.detectLanguageCode()
                            withStyle(style = SpanStyle(localeList = LocaleList(languageCode))) {
                                append(secondaryTitle)
                            }
                        },
                        style = DiiaTextStyle.t3TextBody,
                        color = BlackAlpha54
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        val weightModifier = when (data.valueAlignment) {
            TableItemLabelAlignment.Start -> Modifier.weight(1f)
            TableItemLabelAlignment.End -> Modifier
        }
        Row(modifier = weightModifier, verticalAlignment = rootAlignment) {
            Column(modifier = weightModifier) {
                if (data.valueAsBase64String != null) {
                    Box(
                        modifier = Modifier.size(100.dp, 50.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        SingIconBase64Subatomic(
                            modifier = Modifier.fillMaxSize(),
                            base64Image = data.valueAsBase64String,
                            signImage = null
                        )
                    }
                } else {
                    if (data.valueWithParams != null) {
                        TextWithParametersAtom(
                            data = data.valueWithParams,
                            style = DiiaTextStyle.t3TextBody,
                            onUIAction = onUIAction
                        )
                    }
                    if (data.valueWithParams == null && data.value != null) {
                        Text(
                            text = buildAnnotatedString {
                                val textContent = data.value
                                val languageCode = textContent.detectLanguageCode()
                                withStyle(style = SpanStyle(localeList = LocaleList(languageCode))) {
                                    append(textContent)
                                }
                            },
                            style = DiiaTextStyle.t3TextBody,
                            color = Black
                        )
                    }
                    data.secondaryValue?.let {
                        Text(
                            text = buildAnnotatedString {
                                val textContent = data.secondaryValue
                                val languageCode = textContent.detectLanguageCode()
                                withStyle(style = SpanStyle(localeList = LocaleList(languageCode))) {
                                    append(textContent)
                                }
                            },
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha54
                        )
                    }
                }
            }
            data.iconRight?.let {
                Box(
                    modifier = Modifier.width(24.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconWithBadge(
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = data.value
                                    )
                                )
                            },
                        image = data.iconRight,
                        contentDescription = data.iconRightCode?.let {
                            stringResource(id = DiiaResourceIcon.getContentDescription(it))
                        }
                    )
                }
            }
        }
    }
}

data class TableItemHorizontalMlcData(
    val actionKey: String = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val supportText: String? = null,
    val title: UiText? = null,
    val secondaryTitle: UiText? = null,
    val value: String? = null,
    val valueWithParams: TextWithParametersData? = null,
    val valueAlignment: TableItemLabelAlignment = TableItemLabelAlignment.Start,
    val secondaryValue: String? = null,
    val valueAsBase64String: String? = null,
    val iconRight: UiText? = null,
    val orientation: Boolean? = false,
    val iconRightCode: String? = null
) : TableBlockItem

fun TableItemHorizontalMlc.toUiModel(): TableItemHorizontalMlcData {
    return TableItemHorizontalMlcData(
        componentId = componentId.orEmpty(),
        title = this.label?.let { UiText.DynamicString(it) },
        secondaryTitle = this.secondaryLabel?.let { UiText.DynamicString(it) },
        value = this.value,
        valueWithParams = if (this.value != null && !this.valueParameters.isNullOrEmpty()) {
            TextWithParametersData(
                text = UiText.DynamicString(this.value.orEmpty()),
                parameters = valueParameters?.map {
                    TextParameter(
                        data = TextParameter.Data(
                            name = it.data?.name.toDynamicStringOrNull(),
                            resource = it.data?.resource.toDynamicStringOrNull(),
                            alt = it.data?.alt.toDynamicStringOrNull()
                        ),
                        type = it.type
                    )
                }
            )
        } else {
            null
        },
        secondaryValue = this.secondaryValue,
        supportText = this.supportingValue,
        valueAsBase64String = this.valueImage,
        iconRight = this.icon?.code?.let { UiText.DynamicString(it) },
        orientation = this.orientation,
        valueAlignment = if (this.orientation == true) TableItemLabelAlignment.End else TableItemLabelAlignment.Start
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_Minimum() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        iconRight = UiText.StringResource(R.drawable.ic_copy),
        title = UiText.DynamicString("Title"),
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label Value")
        ),
    )
    TableItemHorizontalMlc(
        data = state
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_OrientationTrue() {
    val data = TableItemHorizontalMlc(
        label = "Title",
        value = "Label Value",
        orientation = true
    )
    TableItemHorizontalMlc(
        data = data.toUiModel()
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_OrientationFalse() {
    val data = TableItemHorizontalMlc(
        label = "Title",
        value = "Label Value",
        orientation = false
    )
    TableItemHorizontalMlc(
        data = data.toUiModel()
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_SupportText() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label Value")
        ),
        supportText = "12"
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        data = state
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_Full() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "С.1.3",
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label Value")
        ),
        secondaryValue = "Secondary label",
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_ValueAsImage() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label Value")
        ),
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemHorizontalMlcPreview_() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label Value")
        ),
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}