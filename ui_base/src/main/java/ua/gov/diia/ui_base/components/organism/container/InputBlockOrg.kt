package ua.gov.diia.ui_base.components.organism.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.ElementPaddingMode
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.input.Validation
import ua.gov.diia.core.models.common_compose.org.container.InputBlockOrg
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrgV2
import ua.gov.diia.core.util.phone.PHONE_NUMBER_VALIDATION_PATTERN
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
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
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrgV2
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneCodeOrgV2Data
import ua.gov.diia.ui_base.components.molecule.input.InputTextMlcV2
import ua.gov.diia.ui_base.components.molecule.input.InputTextMlcV2Data
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgV2
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgV2Data
import ua.gov.diia.ui_base.components.molecule.input.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.BackgroundMode
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.theme.Grey
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun InputBlockOrg(
    modifier: Modifier = Modifier,
    data: InputBlockOrgData,
    onUIAction: (UIAction) -> Unit
) {

    val localFocusManager: FocusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.tableMainHeadingMlc?.let {
            TableMainHeadingMlc(
                modifier = Modifier,
                data = data.tableMainHeadingMlc,
                onUIAction = onUIAction
            )
        }
        data.tableSecondaryHeadingMlc?.let {
            TableSecondaryHeadingMlc(
                modifier = Modifier,
                data = data.tableSecondaryHeadingMlc,
                onUIAction = onUIAction
            )
        }
        data.items.forEachIndexed { index, item ->
            when (item) {
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

                is InputPhoneCodeOrgV2Data -> InputPhoneCodeOrgV2(
                    modifier = Modifier,
                    data = item,
                    onUIAction = onUIAction
                )

                is SelectorOrgV2Data -> SelectorOrgV2(
                    modifier = Modifier,
                    data = item,
                    onUIAction = onUIAction
                )
                else -> {}
            }
        }
        data.attentionIconMessageMlc?.let {
            AttentionIconMessageMlc(
                modifier = Modifier,
                data = data.attentionIconMessageMlc.copy(
                    paddingTop = TopPaddingMode.NONE,
                    paddingHorizontal = SidePaddingMode.NONE
                ),
                onUIAction = onUIAction
            )
        }
    }
}

data class InputBlockOrgData(
    val id: String? = null,
    val actionKey: String = UIActionKeysCompose.INPUT_BLOCK_ORG,
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val tableMainHeadingMlc: TableMainHeadingMlcData? = null,
    val tableSecondaryHeadingMlc: TableSecondaryHeadingMlcData? = null,
    val items: SnapshotStateList<UIElementData>,
    val attentionIconMessageMlc: AttentionIconMessageMlcData? = null
) : UIElementData {

    fun isFormFilledAndValid(): Boolean {
        var result = true
        this.items.forEach {
            when (it) {
                is InputTextMlcV2Data -> {
                    result = if (it.mandatory == true) {
                        it.validation == UIState.Validation.Passed
                    } else {
                        it.validation == UIState.Validation.Passed ||
                                it.validation == UIState.Validation.NeverBeenPerformed ||
                                it.validation == UIState.Validation.InProgress
                    }
                }

                is InputPhoneCodeOrgV2Data -> {
                    if (it.validation != UIState.Validation.Passed) {
                        result = false
                    }
                }

                is SelectorOrgV2Data -> {
                    if (!it.validation) result = false
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
    ): InputBlockOrgData {
        val data = this
        if (id == null) return this
        return this.copy(items = SnapshotStateList<UIElementData>().apply {
            data.items.forEach { item ->
                when (item) {
                    is InputTextMlcV2Data -> {
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
                }
            }
        })
    }

    fun onClearInput(
        id: String?,
        newCountryCode: String? = null
    ): InputBlockOrgData {
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
                }
            }
        })
    }
}

fun InputBlockOrg.toUIModel(): InputBlockOrgData {
    val items = items
    return InputBlockOrgData(
        id = this.componentId,
        componentId = this.componentId.toDynamicStringOrNull(),
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
        tableMainHeadingMlc = this.tableMainHeadingMlc?.toUIModel(),
        tableSecondaryHeadingMlc = this.tableSecondaryHeadingMlc?.toUIModel(),
        items = SnapshotStateList<UIElementData>().apply {
            items?.forEach { item ->
                item.inputTextMlcV2?.let {
                    add(it.toUIModel())
                }
                item.inputPhoneCodeOrgV2?.let {
                    add(it.toUIModel())
                }
                item.selectorOrgV2?.let {
                    add(it.toUIModel())
                }
            }
        },
        attentionIconMessageMlc = this.attentionIconMessageMlc?.toUiModel()
    )
}

fun generateInputBlockOrgData(): InputBlockOrgData {
    val mappedValue = genPhoneField().toUIModel()
    val data = InputBlockOrgData(
        id = "007",
        componentId = UiText.DynamicString("007"),
        paddingTop = TopPaddingMode.MEDIUM,
        paddingHorizontal = SidePaddingMode.MEDIUM,
        tableMainHeadingMlc = TableMainHeadingMlcData(
            title = "Heading".toDynamicString(),
            description = "Description".toDynamicString(),
            iconAtmData = IconAtmData(
                code = "copy"
            )
        ),
        tableSecondaryHeadingMlc = TableSecondaryHeadingMlcData(
            paddingTop = TopPaddingMode.LARGE,
            paddingHorizontal = SidePaddingMode.NONE,
            title = "Heading".toDynamicString(),
            description = "Description".toDynamicString(),
            iconAtmData = IconAtmData(
                code = "copy"
            )
        ),
        items = SnapshotStateList<UIElementData>().apply {
            add(
                mappedValue
            )
            add(
                InputTextMlcV2Data(
                    id = "",
                    label = "label",
                    inputValue = "",
                    placeholder = "Placeholder",
                    hint = "Hint message",
                    validationData = listOf(
                        InputTextMlcV2Data.ValidationTextItem(
                            regex = PHONE_NUMBER_VALIDATION_PATTERN,
                            flags = listOf(),
                            errorMessage = LoremIpsum(40).values.joinToString()
                        )
                    ),
                    validation = UIState.Validation.NeverBeenPerformed,
                    paddingTop = TopPaddingMode.MEDIUM,
                    paddingHorizontal = SidePaddingMode.NONE
                )
            )
            add(
                SelectorOrgV2Data(
                    componentId = "",
                    paddingHorizontal = SidePaddingMode.NONE,
                    label = "Label",
                    value = null,
                    placeholder = "Placeholder",
                    hint = "Hint message",
                )
            )
        },
        attentionIconMessageMlc = AttentionIconMessageMlcData(
            icon = SmallIconAtmData(code = DiiaResourceIcon.ELLIPSE_INFO.code),
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            backgroundMode = BackgroundMode.NOTE
        )
    )

    return data
}

@Preview
@Composable
fun InputBlockOrg_AllActive() {
    var state by remember { mutableStateOf(generateInputBlockOrgData()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        InputBlockOrg(
            data = state
        ) {
            it.data
        }
    }
}

fun genPhoneField(): InputPhoneCodeOrgV2 {
    return InputPhoneCodeOrgV2(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlcV2 = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlcV2(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = null,
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = "00 000 0000",
            paddingMode = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = false,
        hint = null,
        codes = listOf(
            InputPhoneCodeOrgV2.Code(
                id = "UA",
                maskCode = "## ### ####",
                placeholder = "00 000 0000",
                label = "+380",
                description = "🇺🇦 Україна (+380)",
                value = "380",
                icon = "🇺🇦",
                validation = listOf(
                    Validation(
                        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
                        flags = listOf("i", "g"),
                        errorMessage =
                            "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
                    )
                )
            )
        ),
        paddingMode = PaddingMode(
            top = ElementPaddingMode.LARGE,
            side = ElementPaddingMode.NONE
        ),
        isDisable = false
    )
}