package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.util.extensions.image_processing.replaceWhiteWithTransparent
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.subatomic.icon.SingIconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha54
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.extensions.language.detectLanguageCode
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun TableItemVerticalMlc(
    modifier: Modifier = Modifier,
    data: TableItemVerticalMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .defaultMinSize(minHeight = 24.dp)
            .semantics {
                testTag = data.componentId
            }
    ) {
        Column(modifier = Modifier) {
            Row(modifier = Modifier) {
                data.supportText?.let {
                    Text(
                        modifier = Modifier
                            .width(30.dp)
                            .semantics {
                                if (it.isBlank()) {
                                    hideFromAccessibility()
                                }
                            },
                        text = data.supportText,
                        style = DiiaTextStyle.t3TextBody,
                        color = Black,
                        textAlign = TextAlign.Right,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                data.pointSupportingValue?.let {
                    Text(
                        modifier = Modifier.width(20.dp),
                        text = data.pointSupportingValue,
                        style = DiiaTextStyle.t3TextBody,
                        color = Black,
                        textAlign = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    data.title?.let {
                        Text(
                            text = buildAnnotatedString {
                                val textContent = data.title.asString()
                                val languageCode = textContent.detectLanguageCode()
                                withStyle(style = SpanStyle(localeList = LocaleList(languageCode))) {
                                    append(textContent)
                                }
                            },
                            style = DiiaTextStyle.t3TextBody,
                            color = Black
                        )
                    }
                    data.titleWithParams?.let {
                        TextWithParametersAtom(
                            data = data.titleWithParams,
                            style = DiiaTextStyle.t3TextBody,
                            onUIAction = onUIAction
                        )
                    }
                    data.secondaryTitle?.let {
                        Text(
                            text = buildAnnotatedString {
                                val textContent = data.secondaryTitle.asString()
                                val languageCode = textContent.detectLanguageCode()
                                withStyle(style = SpanStyle(localeList = LocaleList(languageCode))) {
                                    append(textContent)
                                }
                            },
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha54
                        )
                    }
                    if (data.valueAsBase64String != null) {
                        Box(
                            modifier = Modifier.size(100.dp, 50.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            SingIconBase64Subatomic(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 4.dp),
                                base64Image = data.valueAsBase64String,
                                signImage = data.signBitmap,
                                contentDescription = context.getString(R.string.accessibility_signature)
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
                                modifier = Modifier.padding(top = if (data.title != null || data.secondaryTitle != null) 4.dp else 0.dp),
                                text = data.value.asString(),
                                style = DiiaTextStyle.t3TextBody,
                                color = Black
                            )
                        }
                        data.secondaryValue?.let {
                            Text(
                                text = buildAnnotatedString {
                                    val textContent = data.secondaryValue.asString()
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
                data.icon?.let {
                    val value = data.value?.asString()
                    Spacer(modifier = Modifier.width(8.dp))
                    IconAtm(
                        modifier = Modifier.size(24.dp),
                        data = data.icon,
                        onUIAction = {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = value,
                                    action = data.icon.action
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

data class TableItemVerticalMlcData(
    val actionKey: String = UIActionKeysCompose.VERTICAL_TABLE_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val supportText: String? = null,
    val pointSupportingValue: String? = null,
    val title: UiText? = null,
    val titleWithParams: TextWithParametersData? = null,
    val secondaryTitle: UiText? = null,
    val value: UiText? = null,
    val valueWithParams: TextWithParametersData? = null,
    val secondaryValue: UiText? = null,
    val valueAsBase64String: String? = null,
    val icon: IconAtmData? = null,
    val signBitmap: ImageBitmap? = null
) : TableBlockItem

fun TableItemVerticalMlc.toUiModel(): TableItemVerticalMlcData {
    return TableItemVerticalMlcData(
        componentId = this.componentId.orEmpty(),
        paddingTop = this?.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this?.paddingMode?.side.toSidePaddingMode(),
        supportText = this.supportingValue,
        pointSupportingValue = this.pointSupportingValue,
        title = this.label?.let { it1 -> UiText.DynamicString(it1) },
        value = this.value?.let { v -> UiText.DynamicString(v) },
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
        secondaryTitle = this.secondaryLabel?.let { it1 -> UiText.DynamicString(it1) },
        secondaryValue = this.secondaryValue?.let { v -> UiText.DynamicString(v) },
        valueAsBase64String = this.valueImage,
        icon = this.icon?.toUiModel(),
        signBitmap = base64ToImageBitmap(this.valueImage)
    )
}

private fun base64ToImageBitmap(base64Image: String?): ImageBitmap? {
    if (base64Image == null) {
        return null
    }
    val byteArray = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        ?.replaceWhiteWithTransparent()
        ?.asImageBitmap()
}

@Composable
@Preview(showBackground = true)
fun TableItemVerticalMlcPreview_ValueAsImage() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        value = UiText.DynamicString("Label Value"),
        secondaryValue = UiText.DynamicString("Secondary label"),
        valueAsBase64String = PreviewBase64Images.sign,
        icon = IconAtmData(
            code = DiiaResourceIcon.COPY.code,
            action = DataActionWrapper(
                type = "copy",
                resource = "123456789"
            )
        )
    )
    TableItemVerticalMlc(
        modifier = Modifier,
        data = data
    )
}


@Composable
@Preview(showBackground = true)
fun TableItemVerticalMlcPreview_secondary_title() {
    val data = TableItemVerticalMlcData(
        id = "123",
        secondaryTitle = UiText.DynamicString("Secondary title"),
    )
    TableItemVerticalMlc(
        modifier = Modifier.fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemVerticalMlcPreview_ValueAsText() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = UiText.DynamicString("Label Value"),
        secondaryValue = UiText.DynamicString("Secondary label"),
    )
    TableItemVerticalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemVerticalMlcPreview_pointSupportingValue() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        pointSupportingValue = "1",
        value = UiText.DynamicString("Label Value"),
        secondaryValue = UiText.DynamicString("Secondary label"),
    )
    TableItemVerticalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview(showBackground = true)
fun TableItemVerticalMlcPreview_ValueParameters() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = UiText.DynamicString("Label Value"),
        valueWithParams = TextWithParametersData(
            text = UiText.DynamicString("Label with params")
        ),
        secondaryValue = UiText.DynamicString("Secondary label"),
    )
    TableItemVerticalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = data
    )
}