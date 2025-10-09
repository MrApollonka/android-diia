package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common.message.StubInfoMessageMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun StubInfoMessageMlc(
    modifier: Modifier = Modifier,
    data: StubInfoMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .border(2.dp, color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString().orEmpty())
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        data.icon?.let {
            IconAtm(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = Black,
                textAlign = TextAlign.Center
            )
        }
        data.text?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class StubInfoMessageMlcData(
    val componentId: UiText? = null,
    val title: UiText?,
    val icon: IconAtmData?,
    val text: UiText?,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

fun StubInfoMessageMlc.toUiModel(): StubInfoMessageMlcData {
    return StubInfoMessageMlcData(
        componentId = this.componentId.toDynamicStringOrNull(),
        icon = this.iconAtm?.toUiModel(),
        text = this.text?.let {
            UiText.DynamicString(it)
        },
        title = this.title?.let {
            UiText.DynamicString(it)
        },
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
    )
}


fun generateStubInfoMessageMlcMockData() = StubInfoMessageMlcData(
    text = UiText.DynamicString(LoremIpsum(words = 20).values.joinToString(" ")),
    title = UiText.DynamicString(LoremIpsum(words = 8).values.joinToString(" ")),
    icon = IconAtmData(code = DiiaResourceIcon.PLACEHOLDER.code),
    componentId = null
)

@Preview(showBackground = true)
@Composable
fun StubInfoMessageMlcPreview() {
    StubInfoMessageMlc(
        data = generateStubInfoMessageMlcMockData(),
        onUIAction = {
            /* no-op */
        }
    )
}