package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun DocNumberCopyMlcLandscape(
    modifier: Modifier = Modifier,
    data: DocNumberCopyMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.value?.let {
                AutoSizeLimitedText(
                    modifier = Modifier.weight(1f, false),
                    text = data.value.asString(),
                    maxLines = 1,
                    color = Black,
                    style = DiiaTextStyle.heroText
                )
            }
            data.icon?.let { icon ->
                val value = data.value?.asString()
                Column(
                    modifier = Modifier
                        .defaultMinSize(24.dp)
                        .align(alignment = Alignment.CenterVertically)
                ) {
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

@Composable
@Preview
fun DocNumberCopyMlcLandscapeDataPreview() {
    val data = DocNumberCopyMlcData(
        id = "123",
        value = "1234567890".toDynamicString(),
        icon = IconAtmData(
            code = DiiaResourceIcon.COPY.code,
            action = DataActionWrapper(
                type = "copy",
                resource = "123456789"
            )
        )
    )
    DocNumberCopyMlcLandscape(
        modifier = Modifier.fillMaxWidth(),
        data = data
    ) {
    }
}

@Composable
@Preview
fun DocNumberCopyMlcLandscapeDataPreview_Long() {
    val data = DocNumberCopyMlcData(
        id = "123",
        value = "1234567890 1234567890".toDynamicString(),
        icon = IconAtmData(
            code = DiiaResourceIcon.COPY.code,
            action = DataActionWrapper(
                type = "copy",
                resource = "123456789"
            )
        )
    )
    DocNumberCopyMlcLandscape(
        modifier = Modifier.fillMaxWidth(),
        data = data
    ) {
    }
}