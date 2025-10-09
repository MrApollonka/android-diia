package ua.gov.diia.ui_base.components.molecule.list.block.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.block.text.TextItemVerticalMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TextItemVerticalMlc(
    modifier: Modifier = Modifier,
    data: TextItemVerticalMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(modifier = modifier.testTag(data.componentId)) {
        data.title?.let {
            Text(
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = Black,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 34.dp),
            verticalAlignment = Alignment.CenterVertically,

            )
        {
            data.value?.let {
                Box(
                    modifier = modifier
                        .height(34.dp)
                        .background(
                            color = BlackSqueeze, shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = it.asString(),
                        style = DiiaTextStyle.t3TextBody,
                        color = Black
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            data.iconRight?.let {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier.width(24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    SmallIconAtm(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }

}

data class TextItemVerticalMlcData(
    val actionKey: String = UIActionKeysCompose.VERTICAL_TEXT_BLOCK_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val title: UiText? = null,
    val value: UiText? = null,
    val iconRight: SmallIconAtmData?
) : TextBlockItem

fun TextItemVerticalMlc.toUiModel(): TextItemVerticalMlcData {
    return TextItemVerticalMlcData(
        id = componentId.orEmpty(),
        componentId = componentId.orEmpty(),
        title = this.label?.let { UiText.DynamicString(it) },
        value = this.value?.let { UiText.DynamicString(it) },
        iconRight = this.iconRight?.toUiModel(),
    )
}

@Composable
@Preview
fun TextItemVerticalMlcDataPreview() {
    val state = TextItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("label"),
        value = UiText.DynamicString("value"),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
            accessibilityDescription = "Button"
        )
    )
    TextItemVerticalMlc(
        data = state
    )
}