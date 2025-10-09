package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.TitleLabelMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TitleLabelMlc(
    modifier: Modifier = Modifier,
    data: TitleLabelMlcData
) {
    Text(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: ""),
        text = data.label,
        style = DiiaTextStyle.h1Heading
    )
}

data class TitleLabelMlcData(
    val actionKey: String = UIActionKeysCompose.TEXT_LABEL_MLC,
    val label: String,
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

fun TitleLabelMlc.toUIModel(): TitleLabelMlcData {
    return TitleLabelMlcData(
        label = this.label,
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
    )
}

fun String?.toComposeTitle(): TitleLabelMlcData? {
    if (this == null) return null
    return TitleLabelMlcData(label = this)
}

fun generateTitleLabelMlcMockData(): TitleLabelMlcData {
    return TitleLabelMlcData(label = "Продаж транспортного засобу")
}

@Preview
@Composable
fun TitleLabelMlcPreview() {
    TitleLabelMlc(
        data = generateTitleLabelMlcMockData()
    )
}