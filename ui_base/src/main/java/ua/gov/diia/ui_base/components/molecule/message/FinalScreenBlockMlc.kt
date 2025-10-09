package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common.message.FinalScreenBlockMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.ExtraLargeIconAtm
import ua.gov.diia.ui_base.components.atom.icon.ExtraLargeIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
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

@Composable
fun FinalScreenBlockMlc(
    modifier: Modifier = Modifier,
    data: FinalScreenBlockMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .testTag(data.componentId?.asString().orEmpty())
            .fillMaxWidth()
            .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        data.icon?.let {
            ExtraLargeIconAtm(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = it.asString(),
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.h2MediumHeading,
                color = Black
            )
        }
        data.subtitle?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = it.asString(),
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.t1BigText,
                color = Black,
            )
        }

    }
}

data class FinalScreenBlockMlcData(
    val componentId: UiText? = null,
    val title: UiText?,
    val icon: ExtraLargeIconAtmData?,
    val subtitle: UiText?,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData

fun FinalScreenBlockMlc.toUiModel(): FinalScreenBlockMlcData {
    return FinalScreenBlockMlcData(
        componentId = this.componentId.toDynamicStringOrNull(),
        icon = this.extraLargeIconAtm?.toUiModel(),
        subtitle = this.subtitle?.let {
            UiText.DynamicString(it)
        },
        title = this.title?.let {
            UiText.DynamicString(it)
        },
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
    )
}


fun generateFinalScreenBlockMlcMockData() = FinalScreenBlockMlcData(
    subtitle = UiText.DynamicString("Дякуємо, заяву підписано"),
    title = UiText.DynamicString("Ваш підпис враховано в колективній заяві на оформлення базової соціальної допомоги\u2028Дочекайтесь надсилання заяви. Заявник збирає підписи усіх членів сімʼї"),
    icon = ExtraLargeIconAtmData(code = DiiaResourceIcon.PLACEHOLDER.code),
    componentId = null
)

@Preview
@Composable
fun FinalScreenBlockMlcPreview() {
    FinalScreenBlockMlc(
        data = generateFinalScreenBlockMlcMockData(),
        onUIAction = {
            /* no-op */
        }
    )
}