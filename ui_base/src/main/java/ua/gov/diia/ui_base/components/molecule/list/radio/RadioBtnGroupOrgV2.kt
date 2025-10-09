package ua.gov.diia.ui_base.components.molecule.list.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.radioBtn.RadioBtnGroupOrgV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnItem
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlcV2
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlcV2Data
import ua.gov.diia.ui_base.components.molecule.checkbox.toUiModel
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun RadioBtnGroupOrgV2(
    modifier: Modifier = Modifier,
    data: RadioBtnGroupOrgV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(4.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.title?.let {
            Text(
                modifier = Modifier.padding(16.dp),
                text = data.title,
                style = DiiaTextStyle.t1BigText
            )
        }


        data.items.forEach { item ->
            if (item is RadioBtnMlcV2Data) {
                val id = data.componentId?.asString()
                RadioBtnMlcV2(
                    modifier = Modifier,
                    data = item,
                ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = item.id,
                            optionalId = item.optionId,
                            optionalType = id //To understand which group the Checkbox is in.
                        )
                    )
                }
            }
        }

        data.button?.let { btn ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                BtnPlainIconAtm(
                    modifier = modifier.padding(vertical = 16.dp),
                    data = btn
                ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = btn.action
                        )
                    )
                }
            }
        }
    }
}

data class RadioBtnGroupOrgV2Data(
    val actionKey: String = UIActionKeysCompose.RADIO_BTN_GROUP_ORG,
    val mandatory: Boolean? = null,
    val title: String? = null,
    val items: List<RadioBtnItem>,
    val button: BtnPlainIconAtmData? = null,
    val componentId: UiText? = null,
    val inputCode: String? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData, Cloneable {
    public override fun clone(): RadioBtnGroupOrgV2Data {
        return super.clone() as RadioBtnGroupOrgV2Data
    }

    fun onItemClick(clickedItemId: String?): RadioBtnGroupOrgV2Data {
        val data = this
        if (clickedItemId == null) return this
        return this.copy(
            items = SnapshotStateList<RadioBtnItem>().apply {
                data.items.forEach {
                    if (it is RadioBtnMlcV2Data) {
                        if (it.id == clickedItemId) {
                            add(it.onRadioButtonClick())
                        } else {
                            add(it.copy(selectionState = UIState.Selection.Unselected))
                        }
                    }
                }
            }
        )
    }

    fun dropSelections(): RadioBtnGroupOrgV2Data {
        val data = this
        return this.copy(
            items = SnapshotStateList<RadioBtnItem>().apply {
                data.items.forEach {
                    if (it is RadioBtnMlcV2Data) {
                        add(it.copy(selectionState = UIState.Selection.Unselected))
                    }
                }
            }
        )
    }

    fun hasSelectedItem(): Boolean {
        return items.any {
            it is RadioBtnMlcV2Data && it.selectionState == UIState.Selection.Selected
        } || items.any {
            it is RadioBtnMlcV2Data && it.selectionState == UIState.Selection.Selected
        }
    }

    fun selectedItems(): List<RadioBtnMlcV2Data> {
        val result = mutableListOf<RadioBtnMlcV2Data>()
        this.items.forEach {
            if (it is RadioBtnMlcV2Data && it.selectionState == UIState.Selection.Selected) {
                result.add(it)
            }
        }
        return result
    }
}

fun RadioBtnGroupOrgV2.toUIModel(): RadioBtnGroupOrgV2Data {
    val entity: RadioBtnGroupOrgV2 = this
    return RadioBtnGroupOrgV2Data(
        title = this.title,
        items = SnapshotStateList<RadioBtnItem>().apply {
            entity.items.forEach { item ->
                item.radioBtnMlcV2?.toUiModel()?.let {
                    add(it)
                }
            }
        },
        button = this.btnPlainIconAtm?.toUiModel(),
        componentId = componentId?.let { UiText.DynamicString(it) },
        inputCode = this.inputCode,
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode()
    )
}

fun generateRadioBtnGroupOrgV2MockData_notitle(): RadioBtnGroupOrgV2Data {
    return RadioBtnGroupOrgV2Data(
        items = SnapshotStateList<RadioBtnMlcV2Data>().apply {
            add(
                RadioBtnMlcV2Data(
                    id = "1",
                    label = "Option 1",
                    description = "Description _ 02.11.2010 / І-БК 123456",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconLeft = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.MENU.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
            add(
                RadioBtnMlcV2Data(
                    id = "2",
                    label = "Option 2",
                    description = "Description _ 02.11.2010 / І-БК 123456",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconRight = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.DELIVERY.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
            add(
                RadioBtnMlcV2Data(
                    id = "3",
                    label = "Option 3",
                    description = "Description _ 02.11.2010 / І-БК 123456",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconBigRight = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.CARD_VISA.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
        },
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
    )
}

fun generateRadioBtnGroupOrgV2MockData_withtitle(): RadioBtnGroupOrgV2Data {
    val source = mutableMapOf(
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3"
    )
    return RadioBtnGroupOrgV2Data(
        title = "Title",
        items = SnapshotStateList<RadioBtnMlcV2Data>().apply {
            source.map {
                add(
                    RadioBtnMlcV2Data(
                        id = it.key,
                        paddingTop = TopPaddingMode.NONE,
                        paddingHorizontal = SidePaddingMode.NONE,
                        label = it.value,
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Selected,
                        iconLeft = SmallIconAtmData(
                            id = "1",
                            code = DiiaResourceIcon.MENU.code,
                            accessibilityDescription = "Button"
                        ),
                        status = "Status"
                    )
                )
            }
        })
}


fun generateRadioBtnGroupOrgV2MockData_resetvalue(): RadioBtnGroupOrgV2Data {
    return RadioBtnGroupOrgV2Data(
        title = "Title",
        items = SnapshotStateList<RadioBtnMlcV2Data>().apply {
            add(
                RadioBtnMlcV2Data(
                    id = "1",
                    label = "Option 1",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconLeft = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.MENU.code,
                        accessibilityDescription = "Button"
                    ),
                    status = "Status"
                )
            )
            add(
                RadioBtnMlcV2Data(
                    id = "2",
                    label = "Option 2",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconRight = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.DELIVERY.code,
                        accessibilityDescription = "Button"
                    ),
                    status = "Status"
                )
            )
            add(
                RadioBtnMlcV2Data(
                    id = "3",
                    label = "Option 3",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                    iconBigRight = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.CARD_VISA.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
        })
}

@Composable
@Preview
fun RadioMoleculeV2Preview_RadioGeneralLogoStartStatusAtom() {
    val state = remember {
        mutableStateOf(generateRadioBtnGroupOrgV2MockData_withtitle())
    }

    RadioBtnGroupOrgV2(modifier = Modifier, data = state.value) {
        state.value = state.value.onItemClick(it.data)
    }
}

@Composable
@Preview
fun RadioMoleculeV2Preview_ResetValue() {
    val state = remember {
        mutableStateOf(generateRadioBtnGroupOrgV2MockData_resetvalue())
    }

    Column {
        RadioBtnGroupOrgV2(modifier = Modifier.fillMaxWidth(), data = state.value) {
            state.value = state.value.onItemClick(it.data)
        }

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = { state.value = state.value.dropSelections() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Reset")
        }
    }

}

@Composable
@Preview
fun RadioBtnGroupOrgV2_Without_title() {
    val source = mutableMapOf(
        "1" to "Title 1", "2" to "Title 2", "3" to "Title 3"
    )
    val data = RadioBtnGroupOrgV2Data(
        items = SnapshotStateList<RadioBtnMlcV2Data>().apply {
            source.map {
                add(
                    RadioBtnMlcV2Data(
                        id = it.key,
                        label = it.value,
                        description = "Description _ 02.11.2010 / І-БК 123456",
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                        iconLeft = SmallIconAtmData(
                            id = "1",
                            code = DiiaResourceIcon.MENU.code,
                            accessibilityDescription = "Button"
                        ),
                    )
                )
            }
        },
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    RadioBtnGroupOrgV2(modifier = Modifier, data = state.value) {
        state.value = state.value.onItemClick(it.data)
    }
}