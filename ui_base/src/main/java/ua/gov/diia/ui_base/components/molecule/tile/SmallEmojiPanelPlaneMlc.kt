package ua.gov.diia.ui_base.components.molecule.tile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SmallEmojiPanelPlaneMlc(
    modifier: Modifier = Modifier,
    data: SmallEmojiPanelPlaneMlcData,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        UiIconWrapperSubatomic(modifier = Modifier.size(16.dp), icon = data.icon)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.text.asString(),
            style = DiiaTextStyle.t2TextDescription,
            color = Black
        )
    }
}


data class SmallEmojiPanelPlaneMlcData(
    val text: UiText,
    val icon: UiIcon,
) : TableBlockItem

fun ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelPlaneMlc?.toUIModel(): SmallEmojiPanelPlaneMlcData? {
    return this?.let {
        val text = it.label ?: return null
        val icon = it.icon ?: return null
        SmallEmojiPanelPlaneMlcData(
            text = UiText.DynamicString(text),
            icon = UiIcon.DrawableResource(icon)
        )
    }
}

@Preview
@Composable
fun SmallEmojiPanelPlaneMlcPreview() {
    SmallEmojiPanelPlaneMlc(
        modifier = Modifier,
        data = SmallEmojiPanelPlaneMlcData(
            text = UiText.DynamicString("Booster vaccine dose"),
            icon = UiIcon.DrawableResource(
                code = DiiaResourceIcon.SYRINGE.code
            )
        )
    )
}