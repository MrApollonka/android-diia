package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.RadioBtnMlcV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnItem
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlueHighlight
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun RadioBtnMlcV2(
    modifier: Modifier = Modifier,
    data: RadioBtnMlcV2Data,
    onUIAction: (UIAction) -> Unit
) {
    val onClick = { onUIAction(UIAction(actionKey = data.actionKey, data = data.id)) }
    val isSelected = data.selectionState == UIState.Selection.Selected

    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
            )
            .conditional(data.selectionState == UIState.Selection.Selected) {
                background(
                    color = BlueHighlight,
                    shape = RoundedCornerShape(16.dp)
                )
            }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .noRippleClickable(
                    enabled = data.interactionState == UIState.Interaction.Enabled
                ) {
                    onClick.invoke()
                }
                .testTag(data.componentId?.asString() ?: ""),
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(
                modifier = Modifier.size(20.dp),
                selected = data.selectionState == UIState.Selection.Selected,
                enabled = data.interactionState == UIState.Interaction.Enabled,
                onClick = {
                    onClick.invoke()
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Black,
                    unselectedColor = Black,
                    disabledSelectedColor = BlackAlpha30,
                    disabledUnselectedColor = BlackAlpha30
                )
            )
            data.iconLeft?.let {
                SmallIconAtm(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    data = it,
                    onUIAction = onUIAction
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 2.dp, start = if (data.iconLeft != null) 8.dp else 16.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = data.label,
                        style = DiiaTextStyle.t1BigText,
                        color = when (data.interactionState) {
                            UIState.Interaction.Disabled -> BlackAlpha30
                            UIState.Interaction.Enabled -> Black
                        }
                    )
                }
                data.description?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        text = data.description,
                        style = DiiaTextStyle.t2TextDescription.copy(
                            lineHeightStyle = LineHeightStyle(
                                LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                        color = BlackAlpha30
                    )

                }
            }
            data.iconRight?.let {
                SmallIconAtm(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(30.dp, 24.dp),
                    data = it,
                    onUIAction = onUIAction
                )
            }

            data.iconBigRight?.let {
                SmallIconAtm(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(56.dp, 36.dp),
                    data = it,
                    onUIAction = onUIAction
                )
            }

            data.status?.let {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = it,
                    style = DiiaTextStyle.t3TextBody,
                    color = BlackAlpha30
                )
            }
        }
    }
}

data class RadioBtnMlcV2Data(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String,
    val label: String,
    val description: String? = null,
    val status: String? = null,
    val optionId: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val iconLeft: SmallIconAtmData? = null,
    val iconRight: SmallIconAtmData? = null,
    val iconBigRight: SmallIconAtmData? = null,
    val dataJson: String? = null,
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData, Cloneable, RadioBtnItem {

    public override fun clone(): RadioBtnMlcData {
        return super.clone() as RadioBtnMlcData
    }

    fun onRadioButtonClick(): RadioBtnMlcV2Data {
        return this.copy(
            selectionState = UIState.Selection.Selected
        )
    }
}

fun RadioBtnMlcV2.toUiModel(): RadioBtnMlcV2Data {
    return RadioBtnMlcV2Data(
        id = this.id ?: this.componentId ?: "",
        label = this.label,
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        description = this.description,
        status = this.status,
        selectionState = if (this.isSelected == true) UIState.Selection.Selected else UIState.Selection.Unselected,
        interactionState = if (this.isEnabled == false) UIState.Interaction.Disabled else UIState.Interaction.Enabled,
        dataJson = this.dataJson,
        iconLeft = this.iconLeft?.toUiModel(),
        iconRight = this.iconRight?.toUiModel(),
        iconBigRight = this.iconBigRight?.toUiModel(),
        optionId = this.dataJson,
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode()
    )
}


@Composable
@Preview(showBackground = true)
fun RadioBtnMlcV2Preview_AllParametersExist() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = "Label",
        description = LoremIpsum(30).values.joinToString(),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}

@Composable
@Preview(showBackground = true)
fun RadioBtnMlcV2Preview_Enabled_Selected() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        label = "Label",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}

@Composable
@Preview
fun RadioBtnMlcV2Preview_Enabled_Unselected() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = "Label",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}

@Composable
@Preview
fun RadioBtnMlcV2Preview_Disabled_Selected() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = "Label",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Selected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}

@Composable
@Preview
fun RadioBtnMlcV2Preview_Disabled_Unselected() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = "Label",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}


@Composable
@Preview
fun RadioBtnMlcV2Preview_LargeLogoRight() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = "Label",
        description = "description",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}

@Composable
@Preview
fun RadioBtnMlcV2Preview_LongLabel() {
    val data = RadioBtnMlcV2Data(
        id = "1",
        label = LoremIpsum(9).values.joinToString(),
        description = LoremIpsum(30).values.joinToString(),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        status = "Status",
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.MENU.code,
            accessibilityDescription = "Button"
        ),
        iconRight = SmallIconAtmData(
            id = "3",
            code = DiiaResourceIcon.DELIVERY.code,
            accessibilityDescription = "Button"
        ),
        iconBigRight = SmallIconAtmData(
            id = "2",
            code = DiiaResourceIcon.CARD_VISA.code,
            accessibilityDescription = "Button"
        ),
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnMlcV2(
        modifier = Modifier, data = state.value
    ) {}
}