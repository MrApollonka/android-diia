package ua.gov.diia.ui_base.components.atom.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.input.InputNumberLargeAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.input.CodeInputType
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha54

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputNumberLargeAtm(
    modifier: Modifier,
    data: InputNumberLargeAtmData,
    imeAction: ImeAction,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val value = data.value.orEmpty()

    val textFieldValueState = remember {
        mutableStateOf(
            TextFieldValue(
                text = value.uppercase(),
                selection = when {
                    value.isEmpty() -> {
                        TextRange.Zero
                    }
                    else -> {
                        TextRange(value.length, value.length)
                    }
                }
            )
        )
    }

    LaunchedEffect(key1 = value) {
        textFieldValueState.value = textFieldValueState.value.copy(
            text = value,
            selection = when {
                value.isEmpty() -> TextRange.Zero
                else -> TextRange(value.length, value.length)
            }
        )
    }

    BasicTextField(modifier = modifier
        .wrapContentWidth()
        .height(104.dp)
        .onFocusChanged {
            focusState = when (focusState) {
                UIState.Focus.NeverBeenFocused -> {
                    if (it.isFocused || it.hasFocus) {
                        UIState.Focus.FirstTimeInFocus
                    } else {
                        UIState.Focus.NeverBeenFocused
                    }
                }

                UIState.Focus.FirstTimeInFocus -> UIState.Focus.OutOfFocus
                UIState.Focus.InFocus -> UIState.Focus.OutOfFocus
                UIState.Focus.OutOfFocus -> UIState.Focus.InFocus
            }
        },
        value = textFieldValueState.value,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = when (data.type) {
                CodeInputType.NUMBER -> KeyboardType.NumberPassword
                CodeInputType.TEXT -> KeyboardType.Text
            }
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                localFocusManager.moveFocus(FocusDirection.Next)
            },
            onDone = {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
        ),
        onValueChange = {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = it.text.lastOrNull()?.toString(),
                    optionalId = data.inputCode
                )
            )
        },
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 38.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        cursorBrush = SolidColor(Black),
        enabled = if (data.state == UIState.Interaction.Disabled) false else true,
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HintText(value, focusState, data.placeholder.orEmpty())
                innerTextField()
            }
        })
}

@Composable
private fun HintText(value: String, focusState: UIState.Focus, hint: String) {
    if (value.isEmpty() && (focusState == UIState.Focus.OutOfFocus || focusState == UIState.Focus.NeverBeenFocused)) {
        Text(
            text = hint,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 38.sp,
                lineHeight = 44.sp
            ), color = BlackAlpha54
        )
    }
}

data class InputNumberLargeAtmData(
    val actionKey: String = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
    var componentId: String? = null,
    val mandatory: Boolean?,
    val value: String?,
    val placeholder: String? = null,
    val inputCode: String? = null,
    val state: UIState.Interaction = UIState.Interaction.Enabled,
    val type: CodeInputType,
) : UIElementData

fun InputNumberLargeAtm.toUIModel(type: CodeInputType = CodeInputType.NUMBER): InputNumberLargeAtmData {
    return InputNumberLargeAtmData(
        componentId = this.componentId,
        state = if (this.state == "disabled") UIState.Interaction.Disabled else UIState.Interaction.Enabled,
        mandatory = this.mandatory,
        value = this.value,
        placeholder = this.placeholder,
        inputCode = this.inputCode,
        type = type
    )
}

@Preview
@Composable
fun InputNumberLargeAtmPreview() {
    ua.gov.diia.ui_base.components.atom.input.InputNumberLargeAtm(
        modifier = Modifier,
        onUIAction = {},
        imeAction = ImeAction.Done,
        data = InputNumberLargeAtmData(
            mandatory = true,
            value = "1",
            state = UIState.Interaction.Enabled,
            type = CodeInputType.NUMBER
        )
    )
}

@Preview
@Composable
fun InputNumberLargeAtmPreview_placeholder() {
    ua.gov.diia.ui_base.components.atom.input.InputNumberLargeAtm(
        modifier = Modifier,
        onUIAction = {},
        imeAction = ImeAction.Done,
        data = InputNumberLargeAtmData(
            mandatory = true,
            value = null,
            placeholder = "*",
            state = UIState.Interaction.Enabled,
            type = CodeInputType.NUMBER
        )
    )
}

@Preview
@Composable
fun InputNumberLargeAtmPreview_disabled() {
    ua.gov.diia.ui_base.components.atom.input.InputNumberLargeAtm(
        modifier = Modifier,
        onUIAction = {},
        imeAction = ImeAction.Done,
        data = InputNumberLargeAtmData(
            mandatory = true,
            value = "4",
            state = UIState.Interaction.Disabled,
            type = CodeInputType.NUMBER
        )
    )
}