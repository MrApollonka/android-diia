package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.AlertCardMlcV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun AlertCardMlcV2(
    modifier: Modifier = Modifier,
    data: AlertCardMlcV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        data.iconAtm?.let {
            IconAtm(data = it)
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            text = data.label.asString(),
            color = Black,
            style = DiiaTextStyle.h4ExtraSmallHeading,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            text = data.text.asString(),
            color = Black,
            style = DiiaTextStyle.t3TextBody,
            textAlign = TextAlign.Center
        )

        data.btnPrimaryAdditionalAtm?.let {
            BtnPrimaryAdditionalAtm(
                data = it,
                onUIAction = onUIAction
            )
        }
    }
}

data class AlertCardMlcV2Data(
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode?,
    val paddingHorizontal: SidePaddingMode?,
    val iconAtm: IconAtmData? = null,
    val label: UiText,
    val text: UiText,
    val btnPrimaryAdditionalAtm: BtnPrimaryAdditionalAtmData?
) : UIElementData

fun AlertCardMlcV2.toUiModel(): AlertCardMlcV2Data {
    return AlertCardMlcV2Data(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
        iconAtm = this.iconAtm.toUiModel(),
        label = this.label.let { UiText.DynamicString(it) },
        text = this.text.let { UiText.DynamicString(it) },
        btnPrimaryAdditionalAtm = this.btnPrimaryAdditionalAtm?.toUIModel()
    )
}

enum class AlertCardMlc2MockType {
    button, withoutbutton;
}

fun generateAlertCardMlc2MockData(mockType: AlertCardMlc2MockType): AlertCardMlcV2Data {
    return when (mockType) {
        AlertCardMlc2MockType.button -> AlertCardMlcV2Data(
            iconAtm = IconAtmData(code = DiiaResourceIcon.DEFAULT_GLOBAL.code),
            label = UiText.DynamicString("Розраховуємо суму допомоги"),
            text = UiText.DynamicString("Це займе не більше 15 хвилин. \nМи повідомимо вас про результат."),
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            btnPrimaryAdditionalAtm = BtnPrimaryAdditionalAtmData(title = UiText.DynamicString("Сповістити"))
        )

        AlertCardMlc2MockType.withoutbutton -> AlertCardMlcV2Data(
            iconAtm = IconAtmData(code = DiiaResourceIcon.DEFAULT_GLOBAL.code),
            label = UiText.DynamicString("Пункт зачинено в робочі години?"),
            text = UiText.DynamicString("Дайте нам знати. Передамо інформацію місцевій владі."),
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            btnPrimaryAdditionalAtm = null
        )
    }
}

@Composable
@Preview
fun AlertCardMlc2Preview() {
    AlertCardMlcV2(
        data = generateAlertCardMlc2MockData(AlertCardMlc2MockType.button)
    ) {}
}

@Composable
@Preview
fun AlertCardMlc2Preview_WithoutBtn() {
    AlertCardMlcV2(
        data = generateAlertCardMlc2MockData(AlertCardMlc2MockType.withoutbutton)
    ) {}
}