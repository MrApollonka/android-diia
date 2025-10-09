package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlcV2
import ua.gov.diia.core.util.phone.PHONE_NUMBER_VALIDATION_PATTERN
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CLEAR_TEXT_INPUT_V2
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.AzureMist
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputTextMlcV2(
    modifier: Modifier = Modifier,
    data: InputTextMlcV2Data,
    imeAction: ImeAction = ImeAction.Default,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()
    val focusRequester = remember { FocusRequester() }

    if (WindowInsets.isImeVisible) {
        LaunchedEffect(Unit) {
            if (data.validation == UIState.Validation.Failed) {
                bringIntoErrorViewRequester.bringIntoView()
            } else if (data.validation != UIState.Validation.Failed && !data.hint.isNullOrEmpty()) {
                bringIntoHintViewRequester.bringIntoView()
            } else {
                bringIntoInputViewRequester.bringIntoView()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (data.showKeyboardFromStart) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = Modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(data.componentId?.asString() ?: "")

    ) {
        Column(
            modifier = Modifier
                .background(
                    color = getColorForBackground(data.isEnabled),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = getColorForBorder(
                        focusState = focusState,
                        data.validation,
                        data.isEnabled
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        )
        {
            BasicTextField(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        focusState = when (focusState) {
                            UIState.Focus.NeverBeenFocused -> {
                                if (data.inputValue.isNullOrEmpty()) {
                                    if (it.isFocused || it.hasFocus) {
                                        UIState.Focus.FirstTimeInFocus
                                    } else {
                                        UIState.Focus.NeverBeenFocused
                                    }
                                } else {
                                    UIState.Focus.OutOfFocus
                                }
                            }

                            UIState.Focus.FirstTimeInFocus -> {
                                UIState.Focus.OutOfFocus
                            }

                            UIState.Focus.InFocus -> UIState.Focus.OutOfFocus
                            UIState.Focus.OutOfFocus -> UIState.Focus.InFocus
                        }
                    }
                    .bringIntoViewRequester(bringIntoInputViewRequester),
                value = data.inputValue.orEmpty(),
                enabled = data.isEnabled,
                onValueChange = { newValue ->
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = newValue,
                            states = listOf(focusState, data.validation),
                            optionalId = data.id
                        )
                    )
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    color = getColorForInput(data.isEnabled)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = imeAction,
                    keyboardType = data.keyboardType
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Next)
                    },
                    onDone = {
                        keyboardController?.let {
                            localFocusManager.clearFocus()
                            it.hide()
                        }
                    }
                ),
                singleLine = true,
                cursorBrush = SolidColor(getColorForInput(data.isEnabled)),
                decorationBox = @Composable { innerTextField ->
                    Box(modifier = Modifier) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag(
                                        data.componentId
                                            ?.asString()
                                            .orEmpty()
                                    ),
                            ) {
                                data.label?.let {
                                    Text(
                                        text = data.label,
                                        style = DiiaTextStyle.t4TextSmallDescription,
                                        color = getColorForLabel(data.validation, data.isEnabled)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                Box(modifier = Modifier) {
                                    if (data.inputValue.isNullOrEmpty()) {
                                        Text(
                                            text = data.placeholder.orEmpty(),
                                            style = DiiaTextStyle.t1BigText,
                                            color = getColorForPlaceholder(
                                                focusState = focusState,
                                                validationState = data.validation
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                            if (data.inputValue?.isNotEmpty() == true && data.isEnabled) {
                                UiIconWrapperSubatomic(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .noRippleClickable {
                                            onUIAction(
                                                UIAction(
                                                    actionKey = data.actionKey,
                                                    action = DataActionWrapper(
                                                        type = CLEAR_TEXT_INPUT_V2,
                                                        resource = data.id
                                                    )
                                                )
                                            )
                                        },
                                    icon = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code),
                                    contentDescription = UiText.StringResource(R.string.clean_search_field),
                                )
                            }
                        }
                    }
                }
            )
        }
        AnimatedVisibility(
            data.validation != UIState.Validation.Failed
        ) {
            data.hint?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)
                        .bringIntoViewRequester(bringIntoHintViewRequester),
                    text = data.hint,
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = BlackAlpha30
                )
            }
        }
        AnimatedVisibility(
            data.validation == UIState.Validation.Failed
        ) {
            data.errorMessage?.let { errorMsg ->
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)
                        .bringIntoViewRequester(bringIntoErrorViewRequester),
                    text = errorMsg,
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = Red
                )
            }
        }
    }
}

private fun getColorForPlaceholder(
    focusState: UIState.Focus,
    validationState: UIState.Validation
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused, UIState.Focus.FirstTimeInFocus -> BlackAlpha30
        UIState.Focus.InFocus, UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> BlackAlpha30
            }

        }
    }
}

private fun getColorForBorder(
    focusState: UIState.Focus,
    validationState: UIState.Validation,
    isEnabled: Boolean
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> if (isEnabled) {
            Primary
        } else {
            AzureMist
        }

        UIState.Focus.FirstTimeInFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> Black
            }
        }

        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> Black
            }
        }

        UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> AzureMist
//                else -> if (inputValue.isNullOrEmpty()) BlackAlpha30 else Black
            }
        }
    }
}

private fun getColorForInput(isEnabled: Boolean): Color {
    return if (isEnabled) {
        Black
    } else {
        BlackAlpha30
    }
}

private fun getColorForLabel(validationState: UIState.Validation, isEnabled: Boolean): Color {
    return when (validationState) {
        UIState.Validation.Failed -> Red
        else -> if (isEnabled) {
            Black
        } else {
            BlackAlpha30
        }
    }
}

private fun getColorForBackground(isEnabled: Boolean): Color {
    return if (isEnabled) {
        White
    } else {
        AzureMist
    }
}

data class InputTextMlcV2Data(
    val actionKey: String = UIActionKeysCompose.TEXT_INPUT_V2,
    val componentId: UiText? = null,
    val id: String? = null,
    val label: String? = null,
    val inputValue: String? = null,
    val placeholder: String? = null,
    val hint: String? = null,
    var errorMessage: String? = null,
    var inputCode: String? = null,
    val validationData: List<ValidationTextItem>? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val isEnabled: Boolean = true,
    val mandatory: Boolean? = null,
    val showKeyboardFromStart: Boolean = false,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val iconRight: UiIcon? = null,
) : InputFormItem() {

    data class ValidationTextItem(
        val regex: String,
        val flags: List<String>,
        val errorMessage: String
    )

    fun onInputChanged(newValue: String?): InputTextMlcV2Data {
        if (newValue == null) return this

        val numRegex = "^[+,0-9]"
        val newValidationValue = if (this.id == "phoneNumber") {
            newValue.filter { n -> numRegex.toRegex().matches(n.toString()) }
        } else {
            newValue
        }

        return this.copy(
            inputValue = newValidationValue,
            validation = when (this.id) {
                "phoneNumber" -> {
                    dataValidation(newValidationValue)
                }

                "email" -> {
                    dataValidationEmail(newValue)
                }

                else -> {
                    dataValidation(newValue)
                }
            }
        )
    }

    fun onClearInput(): InputTextMlcV2Data {
        return this.copy(
            inputValue = "",
            validation = UIState.Validation.InProgress,
        )
    }

    private fun dataValidation(value: String): UIState.Validation {
        var isMatches: Boolean? = null
        //in case no validation data, we assume it is passed
        if (validationData.isNullOrEmpty()) {
            return UIState.Validation.Passed
        }
        this.validationData.forEach {
            if (value.matches(Regex(it.regex))) {
                isMatches = true
            } else {
                isMatches = false
                this.errorMessage = it.errorMessage
                return@forEach
            }
        }
        return if (value.isEmpty()) {
            UIState.Validation.NeverBeenPerformed
        } else if (isMatches != null && isMatches == true) {
            UIState.Validation.Passed
        } else {
            UIState.Validation.Failed
        }
    }

    private fun dataValidationEmail(value: String): UIState.Validation {

        var isMatches: Boolean? = null
        if (!validationData.isNullOrEmpty()) {
            for (validationTextItem in validationData) {
                if (value.matches(Regex(validationTextItem.regex))) {
                    isMatches = true
                } else {
                    isMatches = false
                    this.errorMessage = validationTextItem.errorMessage
                    break
                }
            }
        }

        return if (value.isEmpty()) {
            UIState.Validation.NeverBeenPerformed
        } else if (isMatches == true) {
            UIState.Validation.Passed
        } else {
            UIState.Validation.Failed
        }
    }
}

private fun getValidationState(regex: String?, input: String?): UIState.Validation {
    var result: UIState.Validation = UIState.Validation.NeverBeenPerformed
    if (input.isNullOrEmpty()) {
        return result
    }
    result = if (input.matches(Regex(regex ?: ".*"))) {
        UIState.Validation.Passed
    } else {
        UIState.Validation.Failed
    }
    return result
}

fun InputTextMlcV2.toUIModel(): InputTextMlcV2Data {
    val regexp = this.validation?.firstOrNull()?.regexp ?: ".*"
    val predefinedValue = this.value ?: ""
    val validationList = this.validation
        ?.takeIf { it.isNotEmpty() }
        ?.map {
            InputTextMlcV2Data.ValidationTextItem(
                regex = it.regexp,
                flags = it.flags,
                errorMessage = it.errorMessage
            )
        }
    return InputTextMlcV2Data(
        componentId = this.componentId.orEmpty().toDynamicString(),
        id = this.componentId.orEmpty(),
        inputCode = this.inputCode,
        label = this.label,
        placeholder = this.placeholder,
        hint = this.hint,
        inputValue = this.value,
        mandatory = this.mandatory,
        validation = getValidationState(regexp, predefinedValue),
        validationData = validationList,
        isEnabled = this.isDisable != true,
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
    )
}



@Composable
@Preview
fun InputTextMlcV2Preview() {
    val data = InputTextMlcV2Data(
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
        paddingTop = null,
        paddingHorizontal = null
    )
    val focusRequester = remember { FocusRequester() }
    val state = remember {
        mutableStateOf(data)
    }

    InputTextMlcV2(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester), data = state.value, onUIAction = {
            state.value = state.value.onInputChanged(it.data)
        })
}

@Composable
@Preview
fun InputTextMlcV2Preview_Prefilled() {
    val data = InputTextMlcV2Data(
        id = "",
        label = "label",
        inputValue = "value",
        placeholder = "Placeholder",
        hint = "Hint message",
        validationData = listOf(
            InputTextMlcV2Data.ValidationTextItem(
                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                flags = listOf(),
                errorMessage = LoremIpsum(40).values.joinToString()
            )
        ),
        keyboardType = KeyboardType.Number,
        validation = UIState.Validation.NeverBeenPerformed,
        paddingTop = null,
        paddingHorizontal = null
    )
    val focusRequester = remember { FocusRequester() }
    val state = remember {
        mutableStateOf(data)
    }
    InputTextMlcV2(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester), data = state.value, onUIAction = {
            state.value = state.value.onInputChanged(it.data)
        })
}

@Composable
@Preview
fun InputTextMlcV2Preview_Error() {
    val data = InputTextMlcV2Data(
        id = "",
        label = "label",
        inputValue = "value",
        placeholder = "Placeholder",
        hint = "Hint message",
        validationData = listOf(
            InputTextMlcV2Data.ValidationTextItem(
                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                flags = listOf(),
                errorMessage = "error message"
            )
        ),
        keyboardType = KeyboardType.Number,
        validation = UIState.Validation.Failed,
        paddingTop = null,
        paddingHorizontal = null
    )
    val focusRequester = remember { FocusRequester() }
    val state = remember {
        mutableStateOf(data)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    InputTextMlcV2(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester), data = state.value, onUIAction = {
            state.value = state.value.onInputChanged(it.data)
        })
}

@Composable
@Preview
fun InputTextMlcV2Preview_Disabled() {
    val data = InputTextMlcV2Data(
        id = "",
        label = "label",
        inputValue = "value",
        placeholder = "Placeholder",
        hint = "Hint message",
        isEnabled = false,
        validationData = listOf(
            InputTextMlcV2Data.ValidationTextItem(
                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                flags = listOf(),
                errorMessage = LoremIpsum(40).values.joinToString()
            )
        ),
        keyboardType = KeyboardType.Number,
        validation = UIState.Validation.NeverBeenPerformed,
        paddingTop = null,
        paddingHorizontal = null
    )
    val focusRequester = remember { FocusRequester() }
    val state = remember {
        mutableStateOf(data)
    }
    InputTextMlcV2(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester), data = state.value, onUIAction = {
            state.value = state.value.onInputChanged(it.data)
        })
}