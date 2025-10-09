package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.input.Validation
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrgV2
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
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
import java.util.regex.Pattern

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputPhoneCodeOrgV2(
    modifier: Modifier = Modifier,
    data: InputPhoneCodeOrgV2Data,
    imeAction: ImeAction = ImeAction.Done,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    val localDensity = LocalDensity.current
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    var justFocused by remember { mutableStateOf(false) }
    var phoneCodeWidth by remember {
        mutableStateOf(0.dp)
    }
    val inputValue = data.inputPhoneMlcV2?.value?.asString()
    var dataValidationState by remember { mutableStateOf(data.validation) }
    val errorBlockVisible by remember {
        derivedStateOf {
            ((focusState == UIState.Focus.OutOfFocus) && dataValidationState == UIState.Validation.Failed)
                    || (focusState == UIState.Focus.InFocus && dataValidationState == UIState.Validation.Failed && justFocused)
        }
    }

    if (WindowInsets.isImeVisible) {
        LaunchedEffect(Unit) {
            if (data.validation == UIState.Validation.Failed) {
                bringIntoErrorViewRequester.bringIntoView()
            } else if (data.validation != UIState.Validation.Failed && data.hint != null) {
                bringIntoHintViewRequester.bringIntoView()
            } else {
                bringIntoInputViewRequester.bringIntoView()
            }
        }
    }
    LaunchedEffect(key1 = data.validation) {
        dataValidationState = data.validation
    }

    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Box(
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    data.label?.let {
                        Text(
                            text = data.label.asString(),
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = getColorForLabel(
                                focusState = focusState,
                                validationState = data.validation,
                                justFocused = justFocused
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        phoneCodeWidth =
                                            with(localDensity) { coordinates.size.width.toDp() }
                                    }
                                    .noRippleClickable {
                                        if (data.codeValueIsEditable != false) {
                                            onUIAction(
                                                UIAction(
                                                    actionKey = data.actionKey,
                                                    action = DataActionWrapper(type = UIActionKeysCompose.INPUT_PHONE_CODE_ORG_V2_COUNTRY_CODE)
                                                )
                                            )
                                        }
                                    }
                            ) {
                                data.codeIcon?.asString()?.let {
                                    Text(
                                        modifier = Modifier.padding(end = 4.dp),
                                        text = "${data.codeIcon.asString()} ",
                                        style = DiiaTextStyle.t1BigText
                                    )
                                }
                                val codeLabel =
                                    data.codes.firstOrNull { it.id == data.codeValueId }?.label
                                        ?: ""
                                Text(
                                    modifier = Modifier.padding(end = 16.dp),
                                    text = codeLabel,
                                    style = DiiaTextStyle.t1BigText,
                                    color = Black
                                )
                                Icon(
                                    modifier = Modifier
                                        .wrapContentSize(Alignment.Center)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(
                                        id = R.drawable.ic_arrow_next
                                    ),
                                    contentDescription = stringResource(R.string.details),
                                    tint = if (data.codeValueIsEditable != false) {
                                        Black
                                    } else {
                                        BlackAlpha30
                                    },
                                )
                            }
                        }
                        data.inputPhoneMlcV2?.let { inputPhoneMlcV2Data ->
                            InputPhoneMlcV2(
                                modifier = Modifier
                                    .onFocusChanged {
                                        focusState = when (focusState) {
                                            UIState.Focus.NeverBeenFocused -> {
                                                if (inputValue == null) {
                                                    if (it.isFocused || it.hasFocus) {
                                                        justFocused = true
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
                                            UIState.Focus.OutOfFocus -> {
                                                justFocused = true
                                                UIState.Focus.InFocus
                                            }
                                        }
                                    }
                                    .padding(start = 12.dp),
                                disableErrorMessageDisplaying = true,
                                imeAction = imeAction,
                                localFocusManager = localFocusManager,
                                keyboardController = keyboardController,
                                data = inputPhoneMlcV2Data
                            ) { uiAction ->
                                justFocused = false
                                onUIAction(uiAction)
                            }
                        }
                    }
                }
                if (inputValue?.isNotEmpty() == true && data.isEnabled) {
                    UiIconWrapperSubatomic(
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        action = DataActionWrapper(
                                            type = UIActionKeysCompose.CLEAR_INPUT_PHONE_V2,
                                            resource =  data.inputPhoneMlcV2.actionKey
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
        AnimatedVisibility(
            visible = !errorBlockVisible
        ) {
            data.hint?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)
                        .bringIntoViewRequester(bringIntoHintViewRequester),
                    text = data.hint.asString(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = BlackAlpha30
                )
            }
        }
        AnimatedVisibility(
            visible = errorBlockVisible
        ) {
            data.errorMessage?.let { errorMsg ->
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)
                        .bringIntoViewRequester(bringIntoErrorViewRequester),
                    text = errorMsg.asString(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = Red
                )
            }
        }
    }
}

data class InputPhoneCodeOrgV2Data(
    val actionKey: String = UIActionKeysCompose.INPUT_PHONE_CODE_ORG_V2,
    val componentId: UiText? = null,
    val label: UiText?,
    val hint: UiText? = null,
    val mandatory: Boolean? = null,
    val inputCode: UiText? = null,
    val inputPhoneMlcV2: InputPhoneMlcV2Data?,
    val codeValueId: String? = null,
    val codeValue: UiText? = null,
    val codeIcon: UiText? = null,
    val codeValueIsEditable: Boolean? = null,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val errorMessage: UiText? = null,
    val codes: List<CodeV2> = emptyList(),
    val isEnabled: Boolean = true,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : InputFormItem() {
    fun onInputChanged(newValue: String?): InputPhoneCodeOrgV2Data {
        val inputPhoneMlcV2Update = this.inputPhoneMlcV2?.onInputChanged(newValue)
        val selectedCode = this.codes.find { it.id == this.codeValueId }
        val validationRules = selectedCode?.validationRules ?: emptyList()
        val validationResult = if (newValue.isNullOrEmpty()) {
            UIState.Validation.Passed to null
        } else {
            dataValidation(
                value = (codeValue as UiText.DynamicString).value + newValue,
                validationRules = validationRules,
            )
        }

        return this.copy(
            validation = validationResult.first,
            inputPhoneMlcV2 = inputPhoneMlcV2Update?.copy(validation = validationResult.first),
            errorMessage = validationResult.second
        )
    }

    fun onClearInput(): InputPhoneCodeOrgV2Data {
        return onInputChanged("")
    }

    fun onCountryCodeChanged(newCodeId: String): InputPhoneCodeOrgV2Data {
        val selectedCode = this.codes.find { it.id == newCodeId }
        val inputPhoneMlcUpdate = this.inputPhoneMlcV2?.copy(
            value = UiText.DynamicString(""),
            validationRules = selectedCode?.validationRules ?: emptyList(),
            mask = selectedCode?.maskCode.toDynamicStringOrNull(),
            placeholder = selectedCode?.placeholder?.toDynamicStringOrNull(),
            validation = UIState.Validation.NeverBeenPerformed
        )
        return this.copy(
            codeValueId = newCodeId,
            codeValue = selectedCode?.value?.toDynamicStringOrNull(),
            codeIcon = selectedCode?.icon?.toDynamicStringOrNull(),
            validation = UIState.Validation.NeverBeenPerformed,
            inputPhoneMlcV2 = inputPhoneMlcUpdate
        )
    }

    fun getFullNumber(): String {
        return "${(this.codeValue as UiText.DynamicString).value}${(this.inputPhoneMlcV2?.value as UiText.DynamicString).value}"
    }
}

private fun dataValidation(
    value: String?,
    validationRules: List<ValidationRule>?,

    ): Pair<UIState.Validation, UiText?> {
    if (validationRules.isNullOrEmpty()) {
        UIState.Validation.Passed
    }
    if (value == null) {
        UIState.Validation.NeverBeenPerformed
    }
    val validationPatterns = mutableListOf<Pair<Pattern, UiText>>().apply {
        validationRules?.forEach {
            val regexp = it.regexp
            if (it.flags.isNullOrEmpty()) {
                add(Pattern.compile(regexp) to it.errorMessage)
            }
            it.flags?.forEach { flag ->
                val flagKey = when (flag) {
                    "i" -> Pattern.CASE_INSENSITIVE
                    else -> null
                }
                if (flagKey != null) {
                    add(Pattern.compile(regexp, flagKey) to it.errorMessage)
                } else {
                    add(Pattern.compile(regexp) to it.errorMessage)
                }
            }
        }
    }
    validationPatterns.forEach { pattern ->
        value?.let {
            if (!it.matches(pattern.first.toRegex())) {
                val validationMessage = validationRules?.let {
                    pattern.second
                }
                return UIState.Validation.Failed to validationMessage
            }
        }
    }
    return UIState.Validation.Passed to null
}

private fun getColorForBackground(isEnabled: Boolean): Color {
    return if (isEnabled) {
        White
    } else {
        AzureMist
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

fun InputPhoneCodeOrgV2.toUIModel(): InputPhoneCodeOrgV2Data {
    val selectedCodeEntity = this.codes?.firstOrNull {
        it.id == this.codeValueId
    }
    return InputPhoneCodeOrgV2Data(
        componentId = this.componentId.orEmpty().toDynamicString(),
        label = this.label?.let { UiText.DynamicString(it) },
        hint = this.hint?.let { UiText.DynamicString(it) },
        mandatory = this.mandatory,
        inputCode = this.inputCode?.let { UiText.DynamicString(it) },
        inputPhoneMlcV2 = InputPhoneMlcV2Data(
            componentId = this.inputPhoneMlcV2?.componentId.orEmpty().toDynamicString(),
            inputCode = this.inputPhoneMlcV2?.inputCode?.let {
                it.toDynamicString()
            },
            label = null,
            placeholder = selectedCodeEntity?.placeholder?.toDynamicStringOrNull()
                ?: this.inputPhoneMlcV2?.placeholder?.let {
                    it.toDynamicString()
                },
            hint = null,
            mask = selectedCodeEntity?.maskCode?.toDynamicStringOrNull(),
            //todo need change
            value = this.inputPhoneMlcV2?.value?.let {
                if (it.length >= 9) {
                    it.take(9).toDynamicString()
                } else {
                    this.inputPhoneMlcV2?.value.toDynamicString()
                }
            },
            mandatory = this.mandatory,
            validation = UIState.Validation.NeverBeenPerformed,
            validationRules = emptyList(),
            isEnabled = true
        ),
        codeValue = selectedCodeEntity?.value.toDynamicStringOrNull() ?: "".toDynamicString(),
        codeValueId = this.codeValueId,
        codeIcon = selectedCodeEntity?.icon?.toDynamicStringOrNull(),
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        codeValueIsEditable = this.codeValueIsEditable,
        codes = this.codes?.map {
            CodeV2(
                id = it.id,
                maskCode = it.maskCode,
                placeholder = it.placeholder,
                label = it.label,
                description = it.description,
                value = it.value,
                icon = it.icon,
                validationRules = mutableListOf<ValidationRule>().apply {
                    it.validation?.forEach { validationItem ->
                        add(
                            ValidationRule(
                                regexp = validationItem.regexp,
                                flags = validationItem.flags,
                                errorMessage = validationItem.errorMessage.toDynamicString()
                            )
                        )
                    }
                },
            )
        } ?: emptyList(),
        validation = UIState.Validation.NeverBeenPerformed,
        isEnabled = true
    )
}

data class CodeV2(
    val id: String,
    val maskCode: String?,
    val placeholder: String?,
    val label: String? = null,
    val description: String,
    val value: String,
    val icon: String,
    val validationRules: List<ValidationRule>? = null,
)

private fun getColorForLabel(
    focusState: UIState.Focus,
    validationState: UIState.Validation,
    justFocused: Boolean
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> BlackAlpha30
        UIState.Focus.FirstTimeInFocus -> Black
        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed ->
                    if (justFocused) Red else Black

                else -> Black
            }
        }

        UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> Black
            }
        }
    }
}

@Preview
@Composable
fun InputPhoneCodeOrgV2Preview_CodeValueEditDisabled() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrgV2(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlcV2 = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlcV2(
            inputCode = null,
            componentId = "phone",
            validation = null,
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = "00 000 0000",
            paddingMode = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = false,
        hint = "hint",
        paddingMode = null,
        isDisable = true,
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
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrgV2(
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun InputPhoneCodeOrgV2Preview_CodeValueEmpty() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrgV2(
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
        paddingMode = null,
        isDisable = false
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrgV2(
        modifier = Modifier,
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun PhoneOrgV2_FromBackValid() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrgV2(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlcV2 = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlcV2(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null,
            paddingMode = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = true,
        hint = null,
        paddingMode = null,
        isDisable = false,
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
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrgV2(
        modifier = Modifier,
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PhoneOrgV2_FromBackValid_WithButtons() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrgV2(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlcV2 = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlcV2(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null,
            paddingMode = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = true,
        hint = null,
        paddingMode = null,
        isDisable = false,
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
            ),
            InputPhoneCodeOrgV2.Code(
                id = "TEST",
                maskCode = "### ### ### ###",
                placeholder = "987 654 321 000",
                label = "+123",
                description = "+123 Тестовий код",
                value = "123",
                icon = "\uD83C\uDDE6\uD83C\uDDF8",
                validation = listOf(
                    Validation(
                        regexp = "^123[0-9]{12}",
                        flags = listOf("i"),
                        errorMessage = "Номер телефону має відповідати формату ### ### ### ###"
                    )
                )
            ),
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    Column {
        InputPhoneCodeOrgV2(
            modifier = Modifier,
            data = state.value,
            localFocusManager = focusManager,
            onUIAction = {
                when (it.action?.type) {
                    UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                        //navigation to search
                    }

                    UIActionKeysCompose.INPUT_PHONE_MLC -> {
                        it.action.let { action ->
                            state.value = state.value.onInputChanged(action.resource)
                        }
                    }
                }
            }
        )

        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("TEST")
        }) {
            Text(color = White, text = "Change country code id to Test")
        }
        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("UA")
        }) {
            Text(color = White, text = "Change country code id to Ukraine")
        }
        Button(onClick = {
            println("Data = ${state.value.getFullNumber()}")
        }) {
            Text(color = White, text = "Get full number")
        }
    }
}