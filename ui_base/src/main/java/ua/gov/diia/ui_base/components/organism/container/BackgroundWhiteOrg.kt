package ua.gov.diia.ui_base.components.organism.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.container.BackgroundWhiteOrg
import ua.gov.diia.core.util.extensions.pop
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtm
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
import ua.gov.diia.ui_base.components.infrastructure.utils.clearFocusOnKeyboardDismiss
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.molecule.input.DateInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.DateInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.InputDateMlcData
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.InputNumberMlc
import ua.gov.diia.ui_base.components.molecule.input.InputNumberMlcData
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrg
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrgData
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrgV2
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrgV2Data
import ua.gov.diia.ui_base.components.molecule.input.InputTextMlcV2
import ua.gov.diia.ui_base.components.molecule.input.InputTextMlcV2Data
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrg
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.generateMockInputNumberMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.ItemReadMlc
import ua.gov.diia.ui_base.components.molecule.list.ItemReadMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.toUiModel
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.toUiModel
import ua.gov.diia.ui_base.components.organism.checkbox.CheckboxCascadeGroupOrg
import ua.gov.diia.ui_base.components.organism.checkbox.CheckboxCascadeGroupOrgData
import ua.gov.diia.ui_base.components.organism.checkbox.CheckboxCascadeOrg
import ua.gov.diia.ui_base.components.organism.checkbox.CheckboxCascadeOrgData
import ua.gov.diia.ui_base.components.organism.checkbox.generateMockCheckboxCascadeGroupOrgData
import ua.gov.diia.ui_base.components.organism.checkbox.toUiModel
import ua.gov.diia.ui_base.components.theme.Grey
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun BackgroundWhiteOrg(
    modifier: Modifier = Modifier,
    data: BackgroundWhiteOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val localFocusManager: FocusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 24.dp)
            )
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        data.items.forEachIndexed { index, item ->
            when (item) {
                is TableMainHeadingMlcData -> {
                    TableMainHeadingMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TableSecondaryHeadingMlcData -> {
                    TableSecondaryHeadingMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TableItemCheckboxMlcData -> {
                    TableItemCheckboxMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is ItemReadMlcData -> {
                    ItemReadMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TextInputMoleculeData -> TextInputMolecule(
                    modifier = Modifier.clearFocusOnKeyboardDismiss(),
                    localFocusManager = localFocusManager,
                    imeAction = if (index == data.items.size - 1) {
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    },
                    data = item,
                    onUIAction = onUIAction
                )

                is InputTextMlcV2Data -> InputTextMlcV2(
                    modifier = Modifier.clearFocusOnKeyboardDismiss(),
                    localFocusManager = localFocusManager,
                    imeAction = if (index == data.items.size - 1) {
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    },
                    data = item,
                    onUIAction = onUIAction
                )

                is DateInputMoleculeData -> {
                    DateInputMolecule(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is SelectorOrgData -> SelectorOrg(
                    modifier = Modifier,
                    data = item,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = item.actionKey,
                                data = item.id,
                                optionalId = item.id //To understand which group the box is in
                            )
                        )
                    }
                )

                is InputPhoneCodeOrgData -> InputPhoneCodeOrg(
                    modifier = Modifier,
                    data = item,
                    onUIAction = onUIAction
                )

                is InputPhoneCodeOrgV2Data -> InputPhoneCodeOrgV2(
                    modifier = Modifier,
                    data = item,
                    onUIAction = onUIAction
                )

                is CheckboxCascadeGroupOrgData -> {
                    CheckboxCascadeGroupOrg(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = item,
                        onUIAction = { uiAction ->
                            onUIAction(
                                uiAction.copy(
                                    actionKey = data.actionKey
                                ).pushBundle(
                                    componentId = data.id
                                )
                            )
                        }
                    )
                }

                is CheckboxCascadeOrgData -> {
                    CheckboxCascadeOrg(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = item,
                        onUIAction = { uiAction ->
                            onUIAction(
                                uiAction.copy(
                                    actionKey = data.actionKey
                                ).pushBundle(
                                    componentId = data.id
                                )
                            )
                        }
                    )
                }

                is AttentionIconMessageMlcData -> {
                    val paddingTop = item.paddingTop ?: TopPaddingMode.NONE
                    val paddingHorizontal = item.paddingHorizontal ?: SidePaddingMode.NONE

                    AttentionIconMessageMlc(
                        modifier = Modifier.fillMaxWidth(),
                        data = item.copy(
                            paddingTop = paddingTop,
                            paddingHorizontal = paddingHorizontal
                        ),
                        onUIAction = onUIAction
                    )
                }

                is HeadingWithSubtitlesMlcData -> {
                    HeadingWithSubtitlesMlc(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TextLabelAtmData -> {
                    TextLabelAtm(
                        modifier = modifier,
                        data = item
                    )
                }

                is InputNumberMlcData -> {
                    InputNumberMlc(
                        modifier = Modifier
                            .clearFocusOnKeyboardDismiss(),
                        data = item,
                        imeAction = if (index == data.items.size - 1) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        },
                        localFocusManager = localFocusManager,
                        onUIAction = onUIAction
                    )
                }

                else -> {}
            }
        }
    }
}

data class BackgroundWhiteOrgData(
    val id: String? = null,
    val actionKey: String = UIActionKeysCompose.BACKGROUND_WHITE_ORG,
    val componentId: UiText? = null,
    val items: SnapshotStateList<UIElementData>,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData {

    fun getComponentId() = (componentId as? UiText.DynamicString)?.value

    fun updateStateByAction(actionBundles: ArrayDeque<ActionBundle>): BackgroundWhiteOrgData {
        val componentId = actionBundles.pop()?.componentId
        val items = this.items
        return this.copy(
            items = SnapshotStateList<UIElementData>().apply {
                items.forEach { element ->
                    when (element) {
                        is TableItemCheckboxMlcData -> {
                            if (element.getId() == componentId) {
                                add(element.onCheckboxClick())
                            } else {
                                add(element)
                            }
                        }

                        is CheckboxCascadeGroupOrgData -> {
                            if (element.componentId == componentId) {
                                add(element.updateStateByAction(actionBundles))
                            } else {
                                element
                            }
                        }

                        is CheckboxCascadeOrgData -> {
                            if (element.componentId == componentId) {
                                add(element.updateStateByAction(actionBundles))
                            } else {
                                add(element)
                            }
                        }

                        else -> {
                            add(element)
                        }
                    }
                }
            }.toMutableStateList()
        )
    }

    fun changeCheckboxState(id: String): BackgroundWhiteOrgData {
        val items = this.items
        return this.copy(
            items = SnapshotStateList<UIElementData>().apply {
                items.forEach { element ->
                    when (element) {
                        is TableItemCheckboxMlcData -> {
                            if (element.getId() == id) {
                                add(element.onCheckboxClick())
                            } else {
                                add(element)
                            }
                        }

                        is CheckboxCascadeGroupOrgData -> {
                            add(element.changeCheckboxCascadeGroupOrgState(id))
                        }

                        is CheckboxCascadeOrgData -> {
                            add(element.changeCheckboxCascadeOrgState(id))
                        }

                        else -> {
                            add(element)
                        }
                    }
                }
            }.toMutableStateList()
        )
    }

    fun filterByQuery(query: String): BackgroundWhiteOrgData {
        val filteredItems = items.map { element ->
            when (element) {
                is CheckboxCascadeGroupOrgData -> element.filterByQuery(query)
                else -> element
            }
        }
        val list = SnapshotStateList<UIElementData>()
        list.addAll(filteredItems)

        return copy(items = list)
    }

    fun isFormFilledAndValid(): Boolean {
        var result = true
        this.items.forEach {
            when (it) {
                is InputGroupMoleculeData -> {
                    it.items.forEach {
                        if (it.validation != UIState.Validation.Passed) {
                            result = false
                        }
                    }
                }

                is TextInputMoleculeData -> {
                    if (it.validation != UIState.Validation.Passed && it.mandatory == true) {
                        result = false
                    }
                }

                is InputTextMlcV2Data -> {
                    if (it.validation != UIState.Validation.Passed && it.mandatory == true) {
                        result = false
                    }
                }

                is DateInputMoleculeData -> {
                    if (it.validationState != UIState.Validation.Passed || it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }

                is SelectorOrgData -> {
                    if (it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }

                is InputPhoneCodeOrgData -> {
                    if (it.validation != UIState.Validation.Passed) {
                        result = false
                    }
                }

                is InputPhoneCodeOrgV2Data -> {
                    if (it.validation != UIState.Validation.Passed) {
                        result = false
                    }
                }

                is InputNumberMlcData -> {
                    if (it.validationState != UIState.Validation.Passed) {
                        result = false
                    }
                }

                else -> {}
            }
        }
        return result
    }

    fun onInputChanged(
        id: String?,
        newValue: String?,
        newCountryCode: String? = null
    ): BackgroundWhiteOrgData {
        val data = this
        if (id == null) return this
        return this.copy(items = SnapshotStateList<UIElementData>().apply {
            data.items.forEach { item ->
                when (item) {
                    is InputGroupMoleculeData ->
                        item.items.forEach {
                            if (it.id == id) {
                                add(it.onInputChanged(newValue))
                            } else {
                                add(it)
                            }
                        }

                    is TextInputMoleculeData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is InputTextMlcV2Data -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is SelectorOrgData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is CheckboxSquareMlcData -> {
                        if (item.id == id) {
                            add(item.onCheckboxClick())
                        } else {
                            add(item)
                        }
                    }

                    is InputDateMlcData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is InputPhoneCodeOrgV2Data -> {
                        if (id == "inputPhoneMlc") {
                            if (newValue != null) {
                                add(item.onInputChanged(newValue))
                            } else if (newCountryCode != null) {
                                add(item.onCountryCodeChanged(newCountryCode))
                            } else {
                                //nothing
                            }
                        } else {
                            add(item)
                        }
                    }

                    is TableSecondaryHeadingMlcData -> {
                        add(item)
                    }

                    is TableMainHeadingMlcData -> {
                        add(item)
                    }

                    is AttentionIconMessageMlcData -> {
                        add(item)
                    }

                    is ItemReadMlcData -> {
                        add(item)
                    }

                    is InputNumberMlcData -> {
                        if (item.componentId == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }
                }
            }
        })
    }

    fun onClearInput(
        id: String?,
        newCountryCode: String? = null
    ): BackgroundWhiteOrgData {
        val data = this
        if (id == null) return this
        return this.copy(items = SnapshotStateList<UIElementData>().apply {
            data.items.forEach { item ->
                when (item) {

                    is InputTextMlcV2Data -> {
                        if (item.id == id) {
                            add(item.onClearInput())
                        } else {
                            add(item)
                        }
                    }

                    is InputPhoneCodeOrgV2Data -> {
                        if (id == "inputPhoneMlc") {
                            add(item.onClearInput())
                            if (newCountryCode != null) {
                                add(item.onCountryCodeChanged(newCountryCode))
                            } else {
                                //nothing
                            }
                        } else {
                            add(item)
                        }
                    }

                    is TableMainHeadingMlcData -> {
                        add(item)
                    }

                    is AttentionIconMessageMlcData -> {
                        add(item)
                    }

                    is ItemReadMlcData -> {
                        add(item)
                    }

                    is InputNumberMlcData -> {
                        if (item.componentId == id) {
                            add(item.clearInput())
                        } else {
                            add(item)
                        }
                    }
                }
            }
        })
    }
}

fun BackgroundWhiteOrg.toUIModel(): BackgroundWhiteOrgData {
    val items = items
    return BackgroundWhiteOrgData(
        id = this.componentId,
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
        componentId = this.componentId.toDynamicStringOrNull(),
        items = SnapshotStateList<UIElementData>().apply {
            items?.forEach { item ->
                item.tableMainHeadingMlc?.let {
                    add(it.toUIModel())
                }
                item.tableSecondaryHeadingMlc?.let {
                    add(it.toUIModel())
                }
                item.tableItemCheckboxMlc?.let {
                    add(it.toUIModel() as UIElementData)
                }
                item.checkboxCascadeGroupOrg?.let {
                    add(it.toUiModel() as UIElementData)
                }
                item.checkboxCascadeOrg?.let {
                    add(it.toUiModel() as UIElementData)
                }
                item.selectorOrg?.let {
                    add(it.toUIModel())
                }
                item.inputTextMlc?.let {
                    add(it.toUIModel())
                }
                item.inputTextMlcV2?.let {
                    add(it.toUIModel())
                }
                item.inputDateMlc?.let {
                    add(it.toUIModel())
                }
                item.inputPhoneCodeOrg?.let {
                    add(it.toUIModel())
                }
                item.inputPhoneCodeOrgV2?.let {
                    add(it.toUIModel())
                }
                item.itemReadMlc?.let {
                    add(it.toUiModel())
                }
                item.attentionIconMessageMlc?.let {
                    add(it.toUiModel() as UIElementData)
                }
                item.headingWithSubtitlesMlc?.let {
                    add(it.toUiModel() as UIElementData)
                }
                item.inputNumberMlc?.let {
                    add(it.toUIModel() as UIElementData)
                }
            }
        }
    )
}

@Preview
@Composable
fun BackgroundWhiteOrg_AllActive() {
    val data = generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.all_active)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}

@Preview
@Composable
fun BackgroundWhiteOrg_TextInput() {
    val data = generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.text_input)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}

@Preview
@Composable
fun BackgroundWhiteOrg_PartActive() {
    val data = generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.part_active)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}

@Preview
@Composable
fun BackgroundWhiteOrgWithInputNumberPreview() {
    var backgroundWhiteOrgData by remember {
        mutableStateOf(generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.input_number))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
            .statusBarsPadding()
    ) {
        BackgroundWhiteOrg(
            data = backgroundWhiteOrgData,
            onUIAction = { uiAction ->
                when (uiAction.actionKey) {
                    UIActionKeysCompose.INPUT_NUMBER_MLC -> {
                        backgroundWhiteOrgData = backgroundWhiteOrgData.onInputChanged(
                            id = uiAction.action?.resource,
                            newValue = uiAction.action?.subresource
                        )
                    }

                    UIActionKeysCompose.CLEAR_INPUT -> {
                        backgroundWhiteOrgData = backgroundWhiteOrgData.onClearInput(
                            id = uiAction.action?.resource
                        )
                    }
                }
            }
        )
    }
}

enum class BackgroundWhiteOrgMockType {
    all_active, part_active, cascade, text_input, input_number
}

fun generateBackgroundWhiteOrgMockData(mockType: BackgroundWhiteOrgMockType): BackgroundWhiteOrgData {
    val tableMainHeadingMlc =
        "Main title".toDynamicString().toTableMainHeadingMlcData()
    val tableSecondaryHeadingMlc =
        "Secondary title".toDynamicString().toTableSecondaryHeadingMlcData()

    val tableMainHeadingMlc1 = TableMainHeadingMlcData(
        title = "Main title".toDynamicString(),
        iconAtmData = IconAtmData(
            code = "delete"
        )
    )

    val row1 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.PRIMARY,
        label = "Освіта:".toDynamicString(),
        value = "Вища".toDynamicString(),
        isEnabled = true
    )
    val row2 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.SECONDARY,
        label = "Заклад освіти:".toDynamicString(),
        value = "Академія кіно, доміно, ВЛК та нових медіа".toDynamicString(),
        isEnabled = true
    )
    val checkboxMlc1 = TableItemCheckboxMlcData(
        componentId = "1".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        mandatory = true
    )
    val checkboxMlc2 = TableItemCheckboxMlcData(
        componentId = "2".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        mandatory = true
    )
    val checkboxMlc3 = TableItemCheckboxMlcData(
        componentId = "2".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected,
        mandatory = true
    )
    val primaryCheckboxLabel = TextLabelAtmData(
        componentId = null,
        mode = TextLabelAtmMode.PRIMARY,
        label = "Київ".toDynamicString(),
        value = null,
        isEnabled = true
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

    return when (mockType) {
        BackgroundWhiteOrgMockType.all_active -> BackgroundWhiteOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                add(tableSecondaryHeadingMlc)
                add(checkboxMlc1)
                add(checkboxMlc2)
            }
        )

        BackgroundWhiteOrgMockType.part_active -> BackgroundWhiteOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                add(tableSecondaryHeadingMlc)
                add(checkboxMlc1)
                add(checkboxMlc3)
            }
        )

        BackgroundWhiteOrgMockType.text_input -> BackgroundWhiteOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc1)
                add(textInputMlc1)
                add(textInputMlc2)
            }
        )

        BackgroundWhiteOrgMockType.cascade -> BackgroundWhiteOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                add(generateMockCheckboxCascadeGroupOrgData())
            }
        )

        BackgroundWhiteOrgMockType.input_number -> BackgroundWhiteOrgData(
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                addAll(
                    elements = List(3) { index ->
                        generateMockInputNumberMlcData(index)
                    }
                )
            },
            paddingTop = TopPaddingMode.LARGE,
            paddingHorizontal = SidePaddingMode.NONE
        )
    }
}