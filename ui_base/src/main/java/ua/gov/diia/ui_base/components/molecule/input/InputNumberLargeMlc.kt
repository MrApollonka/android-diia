package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.input.InputNumberLargeMlc
import ua.gov.diia.ui_base.components.atom.input.InputNumberLargeAtmData
import ua.gov.diia.ui_base.components.atom.input.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.clearFocusOnKeyboardDismiss
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputNumberLargeMlc(
    modifier: Modifier = Modifier,
    data: InputNumberLargeMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val localFocusManager: FocusManager = LocalFocusManager.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val countOfItems = data.items.size
    val lastIndex = countOfItems - 1

    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .focusable(true)
            .focusTarget(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.items.forEachIndexed { index, atm ->
            ua.gov.diia.ui_base.components.atom.input.InputNumberLargeAtm(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
                    .weight(1f)
                    .onKeyEvent {
                        if (it.key == Key.Backspace) {
                            val currentValue = atm.value
                            if (index == 0 && currentValue.isNullOrBlank()) {
                                localFocusManager.clearFocus()
                                keyboardController?.hide()
                            } else {
                                val previousAtm = data.items.getOrNull(index - 1)
                                if (currentValue.isNullOrBlank() && previousAtm != null) {
                                    moveFocus(FocusDirection.Previous, localFocusManager)
                                    onUIAction(
                                        UIAction(
                                            actionKey = previousAtm.actionKey,
                                            data = null,
                                            optionalId = previousAtm.inputCode
                                        )
                                    )
                                }
                            }
                        }
                        false
                    }
                    .clearFocusOnKeyboardDismiss()
                    .semantics { testTag = data.componentId.orEmpty() },
                data = atm,
                imeAction = getImeActionByIndex(index, lastIndex),
                localFocusManager = localFocusManager,
                keyboardController = keyboardController,
            ) {
                if (!it.data.isNullOrEmpty()) {
                    if (index == lastIndex) {
                        keyboardController?.hide()
                    } else {
                        if (moveFocus(FocusDirection.Next, localFocusManager).not()) {
                            keyboardController?.hide()
                        }
                    }
                }

                onUIAction(it)
            }
            if (index != lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

private fun moveFocus(direction: FocusDirection, manager: FocusManager): Boolean {
    return try {
        manager.moveFocus(direction)
    } catch (e: Exception) {
        false
    }
}

private fun getImeActionByIndex(index: Int, lastIndex: Int) = if (index != lastIndex) {
    ImeAction.Next
} else {
    ImeAction.Done
}

enum class CodeInputType {
    NUMBER, TEXT
}

data class InputNumberLargeMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
    var componentId: String? = null,
    val type: CodeInputType,
    val mandatoryCounter: Int,
    val items: List<InputNumberLargeAtmData>
) : UIElementData {
    fun onValueChange(inputCode: String, inputValue: String): InputNumberLargeMlcData {
        val elements = mutableListOf<InputNumberLargeAtmData>()
        this.items.forEach {
            if (it.inputCode == inputCode) {
                elements.add(it.copy(value = inputValue))
            } else {
                elements.add(it)
            }
        }
        return this.copy(items = elements)
    }
}

fun InputNumberLargeMlc?.toUIModel(): InputNumberLargeMlcData? {
    if (this == null) return null
    val elements = mutableListOf<InputNumberLargeAtmData>()
    items?.forEach {
        it.inputNumberLargeAtm?.let { e ->
            elements.add(e.toUIModel())
        }
    }
    val mandatoryCounter = this.mandatoryCounter ?: elements.count { it.mandatory ?: false }

    return InputNumberLargeMlcData(
        type = CodeInputType.NUMBER,
        componentId = this.componentId,
        items = elements,
        mandatoryCounter = mandatoryCounter
    )
}

fun generateInputNumberLargeMlcMockData(codeInputType: CodeInputType): InputNumberLargeMlcData {
    return when (codeInputType) {
        CodeInputType.NUMBER -> InputNumberLargeMlcData(
            actionKey = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
            type = CodeInputType.NUMBER,
            items = generateInputAtmNumbers(),
            mandatoryCounter = 4
        )

        CodeInputType.TEXT -> InputNumberLargeMlcData(
            actionKey = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
            type = CodeInputType.TEXT,
            items = generateInputAtmText(),
            mandatoryCounter = 4
        )
    }
}

fun generateInputAtmText(): List<InputNumberLargeAtmData> {
    return mutableListOf<InputNumberLargeAtmData>().apply {
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "A",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "one"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "2",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "two"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "6",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "three"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = null,
                placeholder = "*",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "four"
            )
        )
    }
}

fun generateInputAtmNumbers(): List<InputNumberLargeAtmData> {
    return mutableListOf<InputNumberLargeAtmData>().apply {
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "1",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "one"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "2",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "two"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = "3",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "three"
            )
        )
        add(
            InputNumberLargeAtmData(
                mandatory = true,
                value = null,
                placeholder = "*",
                state = UIState.Interaction.Enabled,
                type = CodeInputType.NUMBER,
                inputCode = "four"
            )
        )
    }
}

@Composable
@Preview
fun InputNumberLargeMlcPreview_Number() {
    val state by remember {
        mutableStateOf(generateInputNumberLargeMlcMockData(CodeInputType.NUMBER))
    }

    Column(modifier = Modifier.background(Primary)) {
        InputNumberLargeMlc(data = state) {}
    }
}

@Composable
@Preview
fun InputNumberLargeMlcPreview_Text() {
    val state by remember {
        mutableStateOf(generateInputNumberLargeMlcMockData(CodeInputType.TEXT))
    }

    Column(modifier = Modifier.background(Primary)) {
        InputNumberLargeMlc(data = state) {}
    }
}
