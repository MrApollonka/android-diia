package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import ua.gov.diia.core.models.common_compose.org.container.BackgroundWhiteOrg
import ua.gov.diia.core.models.common_compose.org.input.RecursiveContainerOrg
import ua.gov.diia.core.models.common_compose.org.input.RecursiveContainerOrgItem
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.organism.container.BackgroundWhiteOrg
import ua.gov.diia.ui_base.components.organism.container.BackgroundWhiteOrgData
import ua.gov.diia.ui_base.components.organism.container.toUIModel
import java.util.UUID

@Composable
fun RecursiveContainerOrg(
    modifier: Modifier = Modifier,
    data: RecursiveContainerOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    var dataLocal by remember(data) { mutableStateOf(data) }

    var showItems by remember { mutableStateOf(false) }

    LaunchedEffect(data.expandState, data.items.size) {
        showItems = false
        if (data.expandState == UIState.Expand.Expanded && data.items.isNotEmpty()) {
            withFrameNanos { }
            showItems = true
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        AnimatedVisibility(visible = dataLocal.expandState == UIState.Expand.Collapsed) {
            SpacerAtm(data = SpacerAtmData(SpacerAtmType.LARGE))
        }

        AnimatedVisibility(
            visible = showItems,
            enter = expandVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                ),
                expandFrom = Alignment.Top,
            ) + slideInVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                ), initialOffsetY = { -it }),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                ), targetOffsetY = { -it }) + shrinkVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                ), shrinkTowards = Alignment.Top
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                dataLocal.items.forEachIndexed { index, item ->
                    BackgroundWhiteOrg(
                        modifier = Modifier,
                        data = item
                    ) {
                        onUIAction(
                            UIAction(
                                actionKey = it.actionKey,
                                data = "${item.id}\\s${it.optionalId}\\s${it.data}",
                                action = it.action
                            )
                        )
                    }
                }
            }
        }
        data.btnWhiteLargeIconAtmData?.let {
            BtnWhiteLargeIconAtm(
                data = it,
                onUIAction = onUIAction
            )
        }
    }
}

data class RecursiveContainerOrgData(
    val actionKey: String = UIActionKeysCompose.RECURSIVE_CONTAINER_ORG,
    val componentId: UiText? = null,
    val expandState: UIState.Expand = UIState.Expand.Expanded,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val items: MutableList<BackgroundWhiteOrgData>,
    val itemsMaxSize: Int?,
    val btnWhiteLargeIconAtmData: BtnWhiteLargeIconAtmData?,
    val template: BackgroundWhiteOrgData?

) : UIElementData, Cloneable {

    public override fun clone(): RecursiveContainerOrgData {
        return super.clone() as RecursiveContainerOrgData
    }

    fun changeBtnState(state: UIState.Interaction): RecursiveContainerOrgData {
        return this.copy(
            btnWhiteLargeIconAtmData = this.btnWhiteLargeIconAtmData?.copy(
                interactionState = state
            )
        )
    }

    fun isFormFilledAndValid(): Boolean {
        return items.all { item ->
            item.isFormFilledAndValid()
        }
    }

    fun expandOrAddItem(): RecursiveContainerOrgData {
        val shouldAddItem = items.isEmpty()

        return when (expandState) {
            UIState.Expand.Collapsed -> {
                if (shouldAddItem && template != null) {
                    val newItem = template.copy(
                        id = UUID.randomUUID().toString(),
                        items = template.items.map {
                            when (it) {
                                is TextInputMoleculeData -> it.copy(
                                    inputValue = "",
                                    validation = UIState.Validation.NeverBeenPerformed
                                )

                                is SelectorOrgData -> it.copy(inputValue = "")
                                else -> it
                            }
                        }.toMutableStateList()
                    )
                    val result = this.copy(
                        expandState = UIState.Expand.Expanded,
                        items = mutableStateListOf(newItem)
                    )
                    return result
                } else {
                    val result = this.copy(expandState = UIState.Expand.Expanded)
                    return result
                }
            }

            UIState.Expand.Expanded -> {
                if (items.size < (itemsMaxSize ?: 10)) {
                    template?.let { templateItem ->
                        val newItem = templateItem.copy(
                            id = UUID.randomUUID().toString(),
                            items = templateItem.items.map {
                                when (it) {
                                    is TextInputMoleculeData -> it.copy(
                                        inputValue = "",
                                        validation = UIState.Validation.NeverBeenPerformed
                                    )

                                    is SelectorOrgData -> it.copy(inputValue = "")
                                    else -> it
                                }
                            }.toMutableStateList()
                        )
                        val updatedItems = items + newItem
                        val result = this.copy(
                            items = updatedItems.toMutableStateList()
                        )
                        return result
                    } ?: this
                } else {
                    val result = this.copy(interactionState = UIState.Interaction.Disabled)
                    return result
                }
            }
        }
    }


    fun onInputChanged(id: String?): RecursiveContainerOrgData {
        if (id.isNullOrEmpty()) return this
        val idList: List<String> = id.split("\\s")
        val newItems = items.map { item ->
            if (item.id == idList[0]) {
                item.onInputChanged(idList[1], idList[2])
            } else {
                item
            }
        }
        return this.copy(
            items = SnapshotStateList<BackgroundWhiteOrgData>().apply {
                addAll(newItems)
            })
    }

    fun onInputChanged(id: String?, newValue: String): RecursiveContainerOrgData {
        if (id.isNullOrEmpty()) return this
        val idList: List<String> = id.split("\\s")
        val newItems = items.map { item ->
            if (item.id == idList[0]) {
                item.onInputChanged(idList[1], newValue)
            } else {
                item
            }
        }
        return this.copy(
            items = SnapshotStateList<BackgroundWhiteOrgData>().apply {
                addAll(newItems)
            })
    }

    fun removeOrCollapseItem(id: String? = null): RecursiveContainerOrgData {
        if (id.isNullOrEmpty()) return this

        val idList: List<String> = id.split("\\s")
        val updatedItems = items.map { item ->
            if (item.id == idList[0]) {
                item.copy(
                    items = item.items.map { inputItem ->
                        when (inputItem) {
                            is TextInputMoleculeData -> inputItem.copy(
                                inputValue = "",
                                validation = UIState.Validation.NeverBeenPerformed
                            )

                            is SelectorOrgData -> inputItem.copy(
                                inputValue = "",
                            )

                            else -> inputItem
                        }
                    }.toMutableStateList()
                )
            } else {
                item
            }
        }.filterNot { it.id == idList[0] }.toMutableStateList()

        return if (items.size == 1) {
            this.copy(
                expandState = UIState.Expand.Collapsed,
                items = updatedItems,
                interactionState = UIState.Interaction.Enabled
            )
        } else {
            this.copy(
                items = updatedItems,
                interactionState = UIState.Interaction.Enabled
            )
        }
    }
}

fun RecursiveContainerOrg?.toUiModel(): RecursiveContainerOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val items = mutableListOf<BackgroundWhiteOrgData>().apply {
        (entity.items as List<RecursiveContainerOrgItem>).forEach { list ->
            if (list.backgroundWhiteOrg is BackgroundWhiteOrg) {
                add(
                    (list.backgroundWhiteOrg as BackgroundWhiteOrg).toUIModel()
                )
            }
        }
    }
    return RecursiveContainerOrgData(
        items = items,
        componentId = entity.componentId.toDynamicStringOrNull(),
        itemsMaxSize = entity.maxNumber,
        btnWhiteLargeIconAtmData = entity.btnWhiteLargeIconAtm?.toUIModel(),
        template = entity.template?.backgroundWhiteOrg?.toUIModel()
    )
}

@Composable
@Preview
fun RecursiveContainerOrgPreview() {
    val tableMainHeadingMlc1 = TableMainHeadingMlcData(
        title = "Main title".toDynamicString(),
        iconAtmData = IconAtmData(
            code = "delete"
        )
    )
    val textInputMlc1 = TextInputMoleculeData(
        id = "11K",
        label = LoremIpsum(2).values.joinToString(),
        inputValue = "",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(5).values.joinToString(),
        validationData = listOf(
            TextInputMoleculeData.ValidationTextItem(
                regex = "[a-zA-Z]",
                flags = listOf(),
                errorMessage = "error",
            )
        ),
        validation = UIState.Validation.NeverBeenPerformed
    )
    val textInputMlc2 = TextInputMoleculeData(
        id = "12B",
        label = LoremIpsum(2).values.joinToString(),
        inputValue = "",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(5).values.joinToString(),
        validationData = listOf(
            TextInputMoleculeData.ValidationTextItem(
                regex = "[a-zA-Z]",
                flags = listOf(),
                errorMessage = "error",
            )
        ),
        validation = UIState.Validation.NeverBeenPerformed
    )
    val backgroundWhiteOrgData = BackgroundWhiteOrgData(
        items = SnapshotStateList<UIElementData>().apply {
            add(tableMainHeadingMlc1)
            add(textInputMlc1)
            add(textInputMlc2)
        }
    )

    val data = RecursiveContainerOrgData(
        btnWhiteLargeIconAtmData = BtnWhiteLargeIconAtmData(label = "Label", id = ""),
        items = SnapshotStateList<BackgroundWhiteOrgData>().apply {
            add(backgroundWhiteOrgData)
            add(backgroundWhiteOrgData)
        },
        itemsMaxSize = 10,
        template = null
    )
    val state = remember {
        mutableStateOf(data)
    }
    RecursiveContainerOrg(data = state.value) {
        when (it.actionKey) {
            "inputForm" -> {
                state.value = state.value.onInputChanged(it.data)
            }

            "textInput" -> {
                state.value = state.value.onInputChanged(it.data)
            }

            UIActionKeysCompose.RECURSIVE_CONTAINER_ORG_EXPAND_AND_ADD -> {
                state.value = state.value.expandOrAddItem()
            }

            "actionRemove" -> {
                state.value = state.value.removeOrCollapseItem(it.data)
            }
        }
    }
}