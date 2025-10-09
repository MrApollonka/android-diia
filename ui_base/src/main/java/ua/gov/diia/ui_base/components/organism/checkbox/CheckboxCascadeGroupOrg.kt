package ua.gov.diia.ui_base.components.organism.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxCascadeGroupOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.SelectedCheckboxCascadeOrg
import ua.gov.diia.core.util.extensions.pop
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.ActionBundle
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.pushBundle

@Composable
fun CheckboxCascadeGroupOrg(
    modifier: Modifier = Modifier,
    data: CheckboxCascadeGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(data.componentId.orEmpty()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (data.items.filterNotNull().isNotEmpty()) {
            data.items.filterNotNull().forEach { item ->
                CheckboxCascadeOrg(
                    modifier = Modifier
                        .fillMaxWidth(),
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

data class CheckboxCascadeGroupOrgData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_CASCADE_GROUP_ORG,
    val componentId: String? = null,
    val inputCode: String? = null,
    val mandatory: Boolean? = null,
    val items: List<CheckboxCascadeOrgData?> = emptyList(),
    val minMandatorySelectedItems: Int? = null
) : UIElementData {

    fun updateStateByAction(actionBundles: ArrayDeque<ActionBundle>): CheckboxCascadeGroupOrgData {
        val componentId = actionBundles.pop()?.componentId
        return this.copy(
            items = items.map { checkboxCascadeOrgData ->
                if (checkboxCascadeOrgData?.componentId == componentId) {
                    checkboxCascadeOrgData?.updateStateByAction(actionBundles)
                } else {
                    checkboxCascadeOrgData
                }
            }
        )
    }

    fun changeCheckboxCascadeGroupOrgState(id: String): CheckboxCascadeGroupOrgData {
        return this.copy(
            items = items.filterNotNull().map { cascade ->
                val parentId = cascade.tableItemCheckboxMlcData?.getId()

                if (parentId == id) {
                    cascade.toggleParentCheckbox()
                } else {
                    val childIndex = cascade.items.indexOfFirst { item -> item?.getId() == id }
                    if (childIndex != -1) {
                        cascade.toggleChildCheckbox(childIndex)
                    } else {
                        cascade
                    }
                }
            }
        )
    }

    fun filterByQuery(query: String): CheckboxCascadeGroupOrgData {
        val filteredItems = items
            .mapNotNull { checkboxCascadeOrgData -> checkboxCascadeOrgData?.filterByQuery(query) }
            .filter { checkboxCascadeOrgData -> checkboxCascadeOrgData.items.isNotEmpty() }

        return copy(items = filteredItems)
    }

}

fun CheckboxCascadeGroupOrg.toUiModel() = CheckboxCascadeGroupOrgData(
    componentId = componentId,
    inputCode = inputCode,
    mandatory = mandatory,
    items = items.map { checkboxCascadeGroupOrgItem ->
        checkboxCascadeGroupOrgItem.checkboxCascadeOrg?.toUiModel()
    },
    minMandatorySelectedItems = minMandatorySelectedItems
)

fun CheckboxCascadeGroupOrgData.getSelectedCheckboxes(): List<String> {
    return items
        .filterNotNull()
        .flatMap { cascade ->
            cascade.getSelectedCheckboxes()
        }
}

fun List<CheckboxCascadeGroupOrgData>.getSelectedCheckboxCascadeGroupOrg(): Map<String, List<SelectedCheckboxCascadeOrg>>? {
    val filteredMap = this
        .mapNotNull { cascadeGroupOrg ->
            val groupCode = cascadeGroupOrg.inputCode ?: return null

            val list = cascadeGroupOrg
                .items
                .mapNotNull { cascadeOrg ->
                    val inputCode = cascadeOrg?.inputCode ?: return null

                    val selectedCheckboxes = cascadeOrg.getSelectedCheckboxes()

                    SelectedCheckboxCascadeOrg(
                        inputCode = inputCode,
                        values = selectedCheckboxes
                    )
                }

            if (list.isEmpty()) null else groupCode to list
        }
        .toMap()

    return filteredMap.ifEmpty { null }
}

fun generateMockCheckboxCascadeGroupOrgData() = CheckboxCascadeGroupOrgData(
    componentId = "checkbox_cascade_group_org_0",
    inputCode = "checkbox_cascade_group_org_input_code_0",
    mandatory = true,
    items = List(3) { id ->
        generateMockCheckboxCascadeOrgData(id = "$id")
    },
    minMandatorySelectedItems = 2
)

@Preview
@Composable
private fun CheckboxCascadeGroupOrgPreview() {
    var checkboxCascadeGroupOrgData by remember {
        mutableStateOf(generateMockCheckboxCascadeGroupOrgData())
    }
    CheckboxCascadeGroupOrg(
        modifier = Modifier
            .statusBarsPadding(),
        data = checkboxCascadeGroupOrgData,
        onUIAction = { uiAction ->
            when (uiAction.actionKey) {
                UIActionKeysCompose.CHECKBOX_CASCADE_GROUP_ORG -> {
                    uiAction.actionBundles.pop() // Retrieve ActionBundle if needed & pop from stack
                    checkboxCascadeGroupOrgData = checkboxCascadeGroupOrgData.updateStateByAction(
                        actionBundles = uiAction.actionBundles
                    )
                }
            }
        }
    )
}