package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWideAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWideAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.mappers.loader.mapToLoader
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun CheckboxBtnOrg(
    modifier: Modifier = Modifier,
    data: CheckboxBtnOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 24.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 24.dp)
            )
            .fillMaxWidth()
            .border(width = 1.dp, color = Primary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        val buttonInProgress =
            progressIndicator.second && (progressIndicator.first == data.buttonData?.id || progressIndicator.first == data.buttonWideData?.id)
        data.options?.forEach { option ->
            val checkBoxData =
                if (buttonInProgress) option.copy(interactionState = UIState.Interaction.Disabled) else option
            CheckboxSquareMlc(
                modifier = if (data.options.last() == option) Modifier else Modifier.padding(bottom = 16.dp),
                data = checkBoxData,
                onUIAction = onUIAction
            )
        }

        data.buttonData?.let {
            BtnPrimaryDefaultAtm(
                modifier = Modifier.fillMaxWidth(),
                data = data.buttonData,
                loader = mapToLoader(progress = progressIndicator),
                onUIAction = onUIAction
            )
        }

        data.buttonWideData?.let {
            BtnPrimaryWideAtm(
                modifier = Modifier.fillMaxWidth(),
                data = data.buttonWideData,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }

        data.btnStrokeWideAtmData?.let {
            BtnStrokeWideAtm(
                modifier = Modifier
                    .fillMaxWidth(),
                data = data.btnStrokeWideAtmData,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }
}

data class CheckboxBtnOrgData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_BTN_ORG,
    val componentId: UiText? = null,
    val id: String = "",
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val options: List<CheckboxSquareMlcData>?,
    val buttonData: BtnPrimaryDefaultAtmData? = null,
    val buttonWideData: BtnPrimaryWideAtmData? = null,
    val btnStrokeWideAtmData: BtnStrokeWideAtmData? = null
) : UIElementData {

    fun onOptionsCheckChanged(optionId: String?): CheckboxBtnOrgData {
        if (optionId == null) return this

        val current = this

        val options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            current.options?.forEach {
                if (optionId == it.id) {
                    add(it.onCheckboxClick())
                } else {
                    add(it)
                }
            }
        } as List<CheckboxSquareMlcData>

        val button = current.buttonData?.copy(
            interactionState = if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                UIState.Interaction.Enabled
            } else {
                UIState.Interaction.Disabled
            }
        )
        val buttonWide = current.buttonWideData?.copy(
            interactionState = if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                UIState.Interaction.Enabled
            } else {
                UIState.Interaction.Disabled
            }
        )
        val btnStrokeWideAtmData = current.btnStrokeWideAtmData?.copy(
            interactionState = if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                UIState.Interaction.Enabled
            } else {
                UIState.Interaction.Disabled
            }
        )

        return this.copy(
            options = options,
            buttonData = button,
            buttonWideData = buttonWide,
            btnStrokeWideAtmData = btnStrokeWideAtmData
        )
    }

    //Use if button should be blocked while externalCondition == false
    fun onOptionsCheckChanged(optionId: String?, externalCondition: Boolean): CheckboxBtnOrgData {
        if (optionId == null) return this

        val current = this

        val options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            current.options?.forEach {
                if (optionId == it.id) {
                    add(it.onCheckboxClick())
                } else {
                    add(it)
                }
            }
        } as List<CheckboxSquareMlcData>

        val button = current.buttonData?.copy(
            interactionState = if (externalCondition) {
                if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            } else {
                UIState.Interaction.Disabled
            }
        )
        val buttonWide = current.buttonWideData?.copy(
            interactionState = if (externalCondition) {
                if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            } else {
                UIState.Interaction.Disabled
            }
        )
        val btnStrokeWideAtmData = current.btnStrokeWideAtmData?.copy(
            interactionState = if (externalCondition) {
                if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            } else {
                UIState.Interaction.Disabled
            }
        )

        return this.copy(
            options = options,
            buttonData = button,
            buttonWideData = buttonWide,
            btnStrokeWideAtmData = btnStrokeWideAtmData
        )
    }

    fun updateButtonStateByCondition(condition: Boolean): CheckboxBtnOrgData {
        return this.copy(
            buttonData = this.buttonData?.copy(
                interactionState = if (condition
                    && (this.options?.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1
                            ||
                            this.options == null)
                ) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            ),
            buttonWideData = this.buttonWideData?.copy(
                interactionState = if (condition
                    && (this.options?.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1
                            ||
                            this.options == null)
                ) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            ),
            btnStrokeWideAtmData = this.btnStrokeWideAtmData?.copy(
                interactionState = if (condition
                    && (this.options?.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1
                            ||
                            this.options == null)
                ) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            )
        )
    }
}

fun CheckboxBtnOrg?.toUIModel(): CheckboxBtnOrgData {
    val checkboxBtnOrg = this
    val button = if (checkboxBtnOrg?.btnPrimaryDefaultAtm != null)
        BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString(checkboxBtnOrg.btnPrimaryDefaultAtm?.label ?: "Далі"),
            id = checkboxBtnOrg.btnPrimaryDefaultAtm?.componentId
                ?: UIActionKeysCompose.BUTTON_REGULAR,
            interactionState = when (checkboxBtnOrg.btnPrimaryDefaultAtm?.state
                ?: ButtonStates.disabled) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Disabled
            },
            action = checkboxBtnOrg.btnPrimaryDefaultAtm?.action?.toDataActionWrapper()
        )
    else null
    val buttonWide = if (checkboxBtnOrg?.btnPrimaryWideAtm != null)
        BtnPrimaryWideAtmData(
            title = UiText.DynamicString(checkboxBtnOrg.btnPrimaryWideAtm?.label ?: "Далі"),
            id = if (checkboxBtnOrg.btnPrimaryWideAtm?.componentId.isNullOrEmpty()) UIActionKeysCompose.BUTTON_REGULAR else checkboxBtnOrg.btnPrimaryWideAtm?.componentId.orEmpty(),
            interactionState = when (checkboxBtnOrg.btnPrimaryWideAtm?.state
                ?: ButtonStates.disabled) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Disabled
            },
            action = checkboxBtnOrg.btnPrimaryWideAtm?.action?.toDataActionWrapper()
        )
    else null

    val checkboxBtnOrgData = CheckboxBtnOrgData(
        componentId = this?.componentId?.let { UiText.DynamicString(it) },
        paddingTop = this?.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this?.paddingMode?.side.toSidePaddingMode(),
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            checkboxBtnOrg?.items?.forEachIndexed() { index, item ->
                add(
                    CheckboxSquareMlcData(
                        id = item.checkboxSquareMlc.id ?: index.toString(),
                        title = UiText.DynamicString(item.checkboxSquareMlc.label),
                        interactionState = if (item.checkboxSquareMlc.blocker != null) {
                            if (item.checkboxSquareMlc.blocker == false) {
                                UIState.Interaction.Enabled
                            } else {
                                UIState.Interaction.Disabled
                            }
                        } else UIState.Interaction.Enabled,
                        selectionState = if (item.checkboxSquareMlc.isSelected == true)
                            UIState.Selection.Selected
                        else
                            UIState.Selection.Unselected
                    )
                )
            }
        },
        buttonData = button,
        buttonWideData = buttonWide,
        btnStrokeWideAtmData = this?.btnStrokeWideAtm?.toUIModel()
    )

    return checkboxBtnOrgData.updateButtonStateByCondition(true)
}

@Preview
@Composable
fun CheckboxBtnOrgPreview() {
    val data = CheckboxBtnOrgData(
        id = "",
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            add(
                CheckboxSquareMlcData(
                    id = "1",
                    title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "2",
                    title = UiText.DynamicString("Дозволяю передати мої персональні дані банку для обробки"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "3",
                    title = UiText.DynamicString("Дозволяю банку телефонувати мені для уточнення даних"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
        },
        buttonData = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Далі"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxBtnOrg(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}

@Preview(widthDp = 600)
@Composable
fun CheckboxBtnOrgPreview_wide() {
    val data = CheckboxBtnOrgData(
        id = "",
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            add(
                CheckboxSquareMlcData(
                    id = "1",
                    title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "2",
                    title = UiText.DynamicString("Дозволяю передати мої персональні дані банку для обробки"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "3",
                    title = UiText.DynamicString("Дозволяю банку телефонувати мені для уточнення даних"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
        },
        buttonWideData = BtnPrimaryWideAtmData(
            title = UiText.DynamicString("Далі"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxBtnOrg(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}

@Preview
@Composable
fun CheckboxBtnOrgPreview_loading() {
    val data = CheckboxBtnOrgData(
        id = "",
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            add(
                CheckboxSquareMlcData(
                    id = "1",
                    title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "2",
                    title = UiText.DynamicString("Дозволяю передати мої персональні дані банку для обробки"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "3",
                    title = UiText.DynamicString("Дозволяю банку телефонувати мені для уточнення даних"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
        },
        buttonData = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Далі"),
            id = "button_id",
            interactionState = UIState.Interaction.Disabled
        ),
        btnStrokeWideAtmData = BtnStrokeWideAtmData(
            componentId = null,
            title = "Label".toDynamicString(),
            action = null
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxBtnOrg(
        data = state.value,
        progressIndicator = Pair("button_id", true)
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}