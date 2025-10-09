package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.SelectorOrgV2
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.card.toUiModel
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.message.AttentionMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.container.InputBlockOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackAlpha40
import ua.gov.diia.ui_base.components.theme.BlackAlpha54
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun SelectorOrgV2(
    modifier: Modifier = Modifier,
    data: SelectorOrgV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(data.componentId)

    ) {
        Row(
            modifier = Modifier
                .background(
                    color = White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = Primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .noRippleClickable {
                    if (data.isEnabled) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.action
                            )
                        )
                    }
                }
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
            ) {
                data.label?.let {
                    Text(
                        text = data.label,
                        style = DiiaTextStyle.t4TextSmallDescription,
                        color = if (data.isEnabled) Black else BlackAlpha30
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (data.value.isNullOrEmpty()) {
                    data.placeholder?.let {
                        Text(
                            text = data.placeholder,
                            style = DiiaTextStyle.t1BigText,
                            color = BlackAlpha30
                        )
                    }
                } else {
                    Text(
                        text = data.value,
                        style = DiiaTextStyle.t1BigText,
                        color = if (data.isEnabled) Black else BlackAlpha30
                    )
                }
            }

            UiIconWrapperSubatomic(
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp)
                    .size(16.dp)
                    .alpha(if (data.isEnabled) 1.0f else 0.3f),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.CHEVRON_SMALL_RIGHT.code),
                contentDescription = UiText.StringResource(R.string.clean_search_field),
            )
        }

        data.hint?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                text = data.hint,
                style = DiiaTextStyle.t4TextSmallDescription,
                color = if (data.isEnabled) BlackAlpha54 else BlackAlpha40
            )
        }
    }
}

data class SelectorOrgV2Data(
    val actionKey: String = UIActionKeysCompose.SELECTOR_ORG_V2,
    val componentId: String,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val inputCode: String? = null,
    val mandatory: Boolean? = null,
    val label: String? = null,
    val placeholder: String? = null,
    val hint: String? = null,
    val valueId: String? = null,
    val value: String? = null,
    val isEnabled: Boolean = true,
    val validation: Boolean = true,
    val action: DataActionWrapper? = null
) : InputFormItem() {

    fun onValueChanged(newValue: String?): SelectorOrgV2Data {
        if (newValue == null) return this
        return this.copy(
            value = newValue,
            validation = isFormFilledAndValid()
        )
    }

    fun isFormFilledAndValid(): Boolean {
        return (mandatory != null && mandatory == true) && value != null
    }
}

fun SelectorOrgV2.toUIModel(): SelectorOrgV2Data {
    return SelectorOrgV2Data(
        componentId = this.componentId,
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
        label = this.label,
        value = this.value,
        placeholder = this.placeholder,
        hint = this.hint,
        inputCode = this.inputCode,
        mandatory = this.mandatory,
        isEnabled = this.isEnabled ?: true,
        validation = (mandatory != null && mandatory == true) && value != null,
        action = action?.toDataActionWrapper()
    )
}

enum class SelectorOrgV2MockType {
    enabled, disabled, enabled_prefilled, disabled_prefilled
}

fun generateSelectorOrgV2MockData(mockType: SelectorOrgV2MockType): SelectorOrgV2Data {
    return when (mockType) {
        SelectorOrgV2MockType.enabled -> {
            SelectorOrgV2Data(
                componentId = "",
                label = "Label",
                value = null,
                placeholder = "Placeholder",
                hint = "Hint message"
            )
        }

        SelectorOrgV2MockType.disabled -> {
            SelectorOrgV2Data(
                componentId = "",
                isEnabled = false,
                label = "Label",
                value = null,
                placeholder = "Placeholder",
                hint = "Hint message"
            )
        }

        SelectorOrgV2MockType.enabled_prefilled -> {
            SelectorOrgV2Data(
                componentId = "",
                label = "Поштове відділення",
                value = "Нова пошта №174",
                placeholder = "Placeholder",
                hint = "Оберіть поштове відділення",
            )
        }

        SelectorOrgV2MockType.disabled_prefilled -> {
            SelectorOrgV2Data(
                componentId = "",
                isEnabled = false,
                label = "Поштове відділення",
                value = "Нова пошта №174",
                placeholder = "Placeholder",
                hint = "Оберіть поштове відділення",
            )
        }
    }

}

@Composable
@Preview
fun SelectorOrgV2_Empty_Enabled() {
    val data = generateSelectorOrgV2MockData(SelectorOrgV2MockType.enabled)
    val state = remember { mutableStateOf(data) }

    SelectorOrgV2(
        modifier = Modifier,
        data = state.value,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SelectorOrgV2_Empty_Disabled() {
    val data = generateSelectorOrgV2MockData(SelectorOrgV2MockType.disabled)
    val state = remember { mutableStateOf(data) }

    SelectorOrgV2(
        modifier = Modifier,
        data = state.value,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SelectorOrgV2_Filled_Enabled() {

    val data = generateSelectorOrgV2MockData(SelectorOrgV2MockType.enabled_prefilled)
    val state = remember { mutableStateOf(data) }

    SelectorOrgV2(
        modifier = Modifier,
        data = state.value,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SelectorOrgV2_Filled_Disable() {

    val data = generateSelectorOrgV2MockData(SelectorOrgV2MockType.disabled_prefilled)
    val state = remember { mutableStateOf(data) }

    SelectorOrgV2(
        modifier = Modifier,
        data = state.value,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}


@Preview
@Composable
fun SelectorOrgV2_Preview() {

    val toolbarData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            TopGroupOrgData(
                navigationPanelMlcData = NavigationPanelMlcData(
                    title = UiText.DynamicString("Title"),
                    isContextMenuExist = true
                )
            )
        )

    val bodyData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            TitleLabelMlcData(label = "Some title"),
            AttentionMessageMlcData(
                icon = "",
                title = "Title",
                description = TextWithParametersData(
                    text = UiText.DynamicString(LoremIpsum(30).values.joinToString()),
                    parameters = emptyList()
                )
            ),

            InputBlockOrgData(
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
                items = SnapshotStateList<UIElementData>().apply {
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
            ),
        )

    val bottomData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            BottomGroupOrgData(
                primaryButton = BtnPrimaryDefaultAtmData(
                    actionKey = "123",
                    title = UiText.DynamicString("title"),
                    interactionState = UIState.Interaction.Enabled
                )
            )
        )


    PublicServiceScreen(
        toolbar = toolbarData,
        body = bodyData,
        bottom = bottomData,
        onEvent = {})

}
