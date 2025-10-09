package ua.gov.diia.ui_base.components.organism.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.container.UpdatedContainerOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnAlertAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtm
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtmData
import ua.gov.diia.ui_base.components.atom.text.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlc
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlcMockType
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2Data
import ua.gov.diia.ui_base.components.molecule.card.toUIModel
import ua.gov.diia.ui_base.components.molecule.card.toUiModel
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlcData
import ua.gov.diia.ui_base.components.molecule.chip.ChipMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrg
import ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData
import ua.gov.diia.ui_base.components.organism.chip.toUiModel
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderSpinnerLoaderAtm
import ua.gov.diia.ui_base.components.theme.Primary

@Composable
fun UpdatedContainerOrg(
    modifier: Modifier = Modifier,
    data: UpdatedContainerOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {

    Box {
        if (progressIndicator.second) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                LoaderSpinnerLoaderAtm()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .testTag(data.componentId?.asString() ?: ""),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                data.items?.forEach { item ->
                    when (item) {
                        is ListItemGroupOrgData -> {
                            ListItemGroupOrg(
                                data = item,
                                onUIAction = onUIAction
                            )
                        }

                        is ChipTabsOrgData -> {
                            ChipTabsOrg(
                                data = item,
                                onUIAction = onUIAction
                            )
                        }

                        is AlertCardMlcData -> {
                            AlertCardMlc(
                                data = item,
                                onUIAction = onUIAction
                            )
                        }
                        is GreyTitleAtmData -> {
                            GreyTitleAtm(
                                data = item
                            )
                        }
                        is CardMlcV2Data -> {
                            CardMlcV2(
                                data = item,
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
            }
        }
    }
}

data class UpdatedContainerOrgData(
    val actionKey: String = UIActionKeysCompose.UPDATED_CONTAINER_ORG,
    val componentId: UiText? = null,
    val id: String? = null, // TODO
    val items: List<UIElementData>? = null,
) : UIElementData {

    //TODO Add change chip state
    fun changeCheckboxState(id: String): UpdatedContainerOrgData {
        val items = this.items
        return this.copy(
            items = SnapshotStateList<UIElementData>().apply {
                items?.forEach {
                    if (it is TableItemCheckboxMlcData && (it.componentId as UiText.DynamicString).value == id) {
                        add(it.onCheckboxClick())
                    } else {
                        add(it)
                    }
                }
            }.toList()
        )
    }
}

fun UpdatedContainerOrg.toUIModel(): UpdatedContainerOrgData {
    val items = items
    return UpdatedContainerOrgData(
        componentId = this.componentId.toDynamicStringOrNull(),
        items = SnapshotStateList<UIElementData>().apply {
            items?.forEach { item ->
                item.listItemGroupOrg?.let {
                    add(it.toUIModel())
                }
                item.chipTabsOrg?.let {
                    add(it.toUiModel())
                }
                item.alertCardMlc?.let {
                    add(it.toUiModel())
                }
                item.greyTitleAtm?.let {
                    add(it.toUIModel())
                }
                item.cardMlcV2?.let {
                    add(it.toUIModel())
                }
            }
        }
    )
}

fun generateListItemGroupOrgData(): List<UIElementData> {
    val list = mutableListOf<UIElementData>()
    val item = ListItemGroupOrgData(
        componentId = "111".toDynamicString(),
        title = UiText.DynamicString("Title:"),
        itemsList = SnapshotStateList<ListItemMlcData>().apply {
            repeat(3) {
                add(
                    ListItemMlcData(
                        id = it.toString(),
                        label = UiText.DynamicString("Label"),
                        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code)
                    )
                )
            }
        }
    )
    list.add(item)
    list.add(generateChipTabsOrgData())
    list.add(generateAlertCardMlcMockData(AlertCardMlcMockType.button))
    return list
}

fun generateChipTabsOrgData(): ChipTabsOrgData {
    return ChipTabsOrgData(chips = SnapshotStateList<ChipMlcData>().apply {
        add(
            ChipMlcData(
                id = "1",
                label = UiText.DynamicString("label 1"),
                code = "inProgress",
                selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipMlcData(
                id = "2",
                label = UiText.DynamicString("label 2"),
                code = "inProgress",
                selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                selectionState = UIState.Selection.Unselected
            )
        )

    })
}

fun generateAlertCardMlcMockData(mockType: AlertCardMlcMockType): AlertCardMlcData {
    return when (mockType) {
        AlertCardMlcMockType.button -> AlertCardMlcData(
            iconText = UiText.DynamicString("⚠️"),
            label = UiText.DynamicString("Пункт зачинено в робочі години?"),
            text = UiText.DynamicString("Дайте нам знати. Передамо інформацію місцевій владі."),
            alertBtn = BtnAlertAdditionalAtmData(title = UiText.DynamicString("Сповістити"))
        )

        AlertCardMlcMockType.withoutbutton -> AlertCardMlcData(
            iconText = UiText.DynamicString("⚠️"),
            label = UiText.DynamicString("Пункт зачинено в робочі години?"),
            text = UiText.DynamicString("Дайте нам знати. Передамо інформацію місцевій владі."),
            alertBtn = null
        )
    }
}

fun generateUpdatedContainerOrgMockData(): UpdatedContainerOrgData {
    return UpdatedContainerOrgData(
        componentId = "111".toDynamicString(),
        items = generateListItemGroupOrgData()
    )
}

@Preview
@Composable
fun UpdatedContainerOrg_ListItemGroupOrg_All() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        UpdatedContainerOrg(
            data = generateUpdatedContainerOrgMockData(),
            progressIndicator = Pair("id", false)
        ) {}
    }
}

@Preview
@Composable
fun UpdatedContainerOrg_ListItemGroupOrg_Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        UpdatedContainerOrg(
            data = generateUpdatedContainerOrgMockData(),
            progressIndicator = Pair("id", true)
        ) {}
    }
}