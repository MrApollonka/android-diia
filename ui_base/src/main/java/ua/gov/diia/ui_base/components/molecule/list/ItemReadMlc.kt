package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.list.ItemReadMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ItemReadMlc(
    modifier: Modifier = Modifier,
    data: ItemReadMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = Black,
            )
            data.value?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = data.value.asString(),
                    style = DiiaTextStyle.h4ExtraSmallHeading,
                    color = Black,
                )
            }
        }
        data.iconRight?.let {
            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                SmallIconAtm(data = it, onUIAction = onUIAction)
            }
        }
    }
}

data class ItemReadMlcData(
    val actionKey: String = UIActionKeysCompose.ITEM_READ_MLC,
    val id: String = "",
    val label: UiText,
    val value: UiText? = null,
    val iconRight: SmallIconAtmData? = null,
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData

fun ItemReadMlc.toUiModel(): ItemReadMlcData {
    return ItemReadMlcData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.componentId ?: UIActionKeysCompose.ITEM_READ_MLC,
        label = label.toDynamicString(),
        value = value?.toDynamicStringOrNull(),
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        iconRight = iconRight?.toUiModel(),
    )
}

fun generateItemReadMlcMockData(): ItemReadMlcData {
    return ItemReadMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        value = UiText.DynamicString("Value"),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
            accessibilityDescription = "Button"
        )
    )
}

@Composable
@Preview
fun ItemReadMlcPreview() {
    val data = generateItemReadMlcMockData()
    ItemReadMlc(modifier = Modifier, data = data) {
    }
}