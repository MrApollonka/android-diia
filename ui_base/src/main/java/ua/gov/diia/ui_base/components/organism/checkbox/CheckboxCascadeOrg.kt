package ua.gov.diia.ui_base.components.organism.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxCascadeOrg
import ua.gov.diia.core.util.extensions.pop
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmData
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.ActionBundle
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.pushBundle
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel

@Composable
fun CheckboxCascadeOrg(
    modifier: Modifier = Modifier,
    data: CheckboxCascadeOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .testTag(data.componentId.orEmpty()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        data.tableItemCheckboxMlcData?.let { item ->
            TableItemCheckboxMlc(
                modifier = Modifier,
                data = item,
                onUIAction = { uiAction ->
                    onUIAction(
                        uiAction.copy(
                            actionKey = data.actionKey
                        ).pushBundle(
                            componentId = data.componentId
                        )
                    )
                }
            )
        }
        if (data.items.filterNotNull().isNotEmpty()) {
            val paddingStart = if (data.tableItemCheckboxMlcData == null) 0.dp else 24.dp
            Column(
                modifier = Modifier
                    .padding(start = paddingStart)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                data.items.filterNotNull().forEach { item ->
                    TableItemCheckboxMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = { uiAction ->
                            onUIAction(
                                uiAction.copy(
                                    actionKey = data.actionKey
                                ).pushBundle(
                                    componentId = data.componentId
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

data class CheckboxCascadeOrgData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_CASCADE_ORG,
    val componentId: String? = null,
    val inputCode: String? = null,
    val mandatory: Boolean? = null,
    val tableItemCheckboxMlcData: TableItemCheckboxMlcData?,
    val items: List<TableItemCheckboxMlcData?> = emptyList(),
    val isEnabled: Boolean? = null,
    val minMandatorySelectedItems: Int? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData {

    fun updateStateByAction(actionBundles: ArrayDeque<ActionBundle>): CheckboxCascadeOrgData {
        val componentId = actionBundles.pop()?.componentId.orEmpty()
        return changeCheckboxCascadeOrgState(componentId)
    }

    fun changeCheckboxCascadeOrgState(id: String): CheckboxCascadeOrgData {
        return if (tableItemCheckboxMlcData?.getId() == id) {
            toggleParentCheckbox()
        } else {
            val childIndex = items.indexOfFirst { item -> item?.getId() == id }
            if (childIndex != -1) {
                toggleChildCheckbox(childIndex)
            } else {
                this
            }
        }
    }

    fun toggleParentCheckbox(): CheckboxCascadeOrgData {
        val currentParent = tableItemCheckboxMlcData ?: return this

        val updatedChildren: List<TableItemCheckboxMlcData?>
        val updatedParent: TableItemCheckboxMlcData

        if (currentParent.selectionState == UIState.Selection.Unselected || currentParent.isNotFullSelected == true) {
            updatedChildren = items.map { child ->
                if (child?.interactionState == UIState.Interaction.Enabled) {
                    child.copy(selectionState = UIState.Selection.Selected)
                } else {
                    child
                }
            }
            updatedParent = currentParent.copy(
                selectionState = UIState.Selection.Selected,
                isNotFullSelected = false
            )
        } else {
            updatedChildren = items.map { child ->
                if (child?.interactionState == UIState.Interaction.Enabled) {
                    child.copy(selectionState = UIState.Selection.Unselected)
                } else {
                    child
                }
            }
            updatedParent = currentParent.copy(
                selectionState = UIState.Selection.Unselected,
                isNotFullSelected = false
            )
        }

        return this.copy(
            tableItemCheckboxMlcData = updatedParent,
            items = updatedChildren
        )
    }

    fun toggleChildCheckbox(toggledIndex: Int): CheckboxCascadeOrgData {
        val updatedChildItems = items.mapIndexed { index, child ->
            if (index == toggledIndex && child != null) child.onCheckboxClick() else child
        }

        val enabledChildren =
            updatedChildItems.filter { it?.interactionState == UIState.Interaction.Enabled }
        val selectedEnabledCount =
            enabledChildren.count { it?.selectionState == UIState.Selection.Selected }

        val updatedParent = when (selectedEnabledCount) {
            0 -> tableItemCheckboxMlcData?.copy(
                selectionState = UIState.Selection.Unselected,
                isNotFullSelected = false
            )

            enabledChildren.size -> tableItemCheckboxMlcData?.copy(
                selectionState = UIState.Selection.Selected,
                isNotFullSelected = false
            )

            else -> tableItemCheckboxMlcData?.copy(
                selectionState = UIState.Selection.Selected,
                isNotFullSelected = true
            )
        }

        return this.copy(
            tableItemCheckboxMlcData = updatedParent,
            items = updatedChildItems
        )
    }

    fun isAnyCheckboxSelected(): Boolean {
        return items.filterNotNull()
            .any { item -> item.selectionState == UIState.Selection.Selected }
    }

    fun isAllMandatoryCheckboxSelected(): Boolean {
        return items.filterNotNull()
            .filter { item -> item.mandatory == true }
            .all { item -> item.selectionState == UIState.Selection.Selected }
    }

    fun filterByQuery(query: String): CheckboxCascadeOrgData {
        val filteredChildren = items
            .filterNotNull()
            .filter { tableItemCheckboxMlcData ->
                tableItemCheckboxMlcData.getLabel()?.contains(query, ignoreCase = true) == true
            }

        return copy(items = filteredChildren)
    }

}

fun CheckboxCascadeOrg.toUiModel() = CheckboxCascadeOrgData(
    componentId = componentId,
    inputCode = inputCode,
    mandatory = mandatory,
    tableItemCheckboxMlcData = tableItemCheckboxMlc.toUIModel(),
    items = items.map { checkboxCascadeItem ->
        checkboxCascadeItem.tableItemCheckboxMlc.toUIModel()
    },
    isEnabled = isEnabled,
    minMandatorySelectedItems = minMandatorySelectedItems,
    paddingTop = paddingMode?.top.toTopPaddingMode(),
    paddingHorizontal = paddingMode?.side.toSidePaddingMode()
)

fun CheckboxCascadeOrgData.getSelectedCheckboxes(): List<String> {
    return items
        .filter { item ->
            item != null && item.selectionState == UIState.Selection.Selected
        }.mapNotNull { item ->
            item?.dataJson ?: item?.getId() ?: item?.getInputCode()
        }
}

fun generateMockCheckboxCascadeOrgData(id: String = "0") = CheckboxCascadeOrgData(
    componentId = "checkbox_cascade_org_$id",
    inputCode = "checkbox_cascade_org_input_code_$id",
    mandatory = true,
    tableItemCheckboxMlcData = TableItemCheckboxMlcData(
        componentId = "table_item_checkbox_mlc_$id".toDynamicString(),
        inputCode = "input_code_$id".toDynamicString(),
        mandatory = false,
        rows = listOf(
            TextLabelAtmData(
                componentId = "text_label_atm_$id".toDynamicString(),
                mode = TextLabelAtmMode.PRIMARY,
                label = "Область".toDynamicString(),
                value = null,
                isEnabled = true
            )
        ),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected
    ),
    items = List(5) {
        TableItemCheckboxMlcData(
            componentId = "table_item_checkbox_mlc_${id}_$it".toDynamicString(),
            inputCode = "input_code_${id}_$it".toDynamicString(),
            mandatory = false,
            rows = listOf(
                TextLabelAtmData(
                    componentId = "text_label_atm_${id}_$it".toDynamicString(),
                    mode = TextLabelAtmMode.PRIMARY,
                    label = "Місто $it".toDynamicString(),
                    value = null,
                    isEnabled = true
                )
            ),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Unselected
        )
    },
    isEnabled = true,
    minMandatorySelectedItems = 2,
    paddingTop = TopPaddingMode.NONE
)

@Preview
@Composable
private fun CheckboxCascadeOrgPreview() {
    var checkboxCascadeOrgData by remember {
        mutableStateOf(generateMockCheckboxCascadeOrgData())
    }
    CheckboxCascadeOrg(
        modifier = Modifier
            .statusBarsPadding(),
        data = checkboxCascadeOrgData,
        onUIAction = { uiAction ->
            when (uiAction.actionKey) {
                UIActionKeysCompose.CHECKBOX_CASCADE_ORG -> {
                    uiAction.actionBundles.pop() // Retrieve ActionBundle if needed & pop from stack
                    checkboxCascadeOrgData = checkboxCascadeOrgData.updateStateByAction(
                        actionBundles = uiAction.actionBundles
                    )
                }
            }
        }
    )
}