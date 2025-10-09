package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.SearchBarOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlc
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUiModel

@Composable
fun SearchBarOrg(
    modifier: Modifier = Modifier,
    data: SearchBarOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                start = data.sidePaddingMode.toDp(defaultPadding = 24.dp),
                top = data.topPaddingMode.toDp(defaultPadding = 8.dp),
                end = data.sidePaddingMode.toDp(defaultPadding = 24.dp),
                bottom = 8.dp,
            )
    ) {
        SearchInputMlc(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f, true),
            data = data.searchInputMlc,
            onUIAction = {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = it.data
                    )
                )
            }
        )
        data.btnWhiteAdditionalIconAtm?.let {
            Spacer(modifier = Modifier.width(16.dp))
            BtnWhiteAdditionalIconAtm(
                modifier = Modifier.align(Alignment.Bottom),
                data = it,
                onUIAction = {
                    onUIAction(it)
                }
            )
        }
    }
}

data class SearchBarOrgData(
    val actionKey: String = UIActionKeysCompose.SEARCH_INPUT,
    val componentId: UiText? = null,
    val searchInputMlc: SearchInputMlcData,
    val btnWhiteAdditionalIconAtm: BtnWhiteAdditionalIconAtmData?,
    val topPaddingMode: TopPaddingMode? = null,
    val sidePaddingMode: SidePaddingMode? = null
) : UIElementData {
    fun onChange(newValue: String?): SearchBarOrgData {
        return this.copy(
            searchInputMlc = searchInputMlc.onChange(newValue)
        )
    }

    fun onBadgeChanged(newValue: Int): SearchBarOrgData {
        return this.copy(
            btnWhiteAdditionalIconAtm = btnWhiteAdditionalIconAtm?.onBadgeChanged(newValue)
        )
    }
}

fun SearchBarOrg.toUiModel(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = this.componentId.toDynamicStringOrNull(),
        searchInputMlc = searchInputMlc.toUiModel(),
        btnWhiteAdditionalIconAtm = btnWhiteAdditionalIconAtm?.toUIModel(),
        topPaddingMode = paddingMode?.top.toTopPaddingMode(),
        sidePaddingMode = paddingMode?.side.toSidePaddingMode()
    )
}

fun generateSearchBarOrgData(): SearchBarOrgData {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            interactionState = UIState.Interaction.Enabled
        )
    )
    return data
}

fun generateSearchBarOrgPreviewEmpty16Padding(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        topPaddingMode = TopPaddingMode.MEDIUM,
        sidePaddingMode = SidePaddingMode.MEDIUM,
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            interactionState = UIState.Interaction.Enabled
        )
    )
}

fun generateSearchBarOrgPreviewNoButton(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = null
    )
}

fun generateSearchInputMlcPreviewEditableWithText(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            searchFieldValue = UiText.DynamicString("Text"),
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            interactionState = UIState.Interaction.Enabled
        )
    )
}

fun generateSearchInputMlcPreviewEditableIconLabel(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            label = "Label".toDynamicStringOrNull(),
            interactionState = UIState.Interaction.Enabled
        )
    )
}

fun generateSearchInputMlcPreviewEditableIconLabelCounter(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            label = "Label".toDynamicStringOrNull(),
            badge = BadgeCounterAtmData(1),
            interactionState = UIState.Interaction.Enabled
        )
    )
}

@Composable
@Preview
fun SearchBarOrgPreview_Empty_24Padding() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchBarOrgData(),
        onUIAction = {
            (UIAction(actionKey = generateSearchBarOrgData().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchBarOrgPreview_Empty_16Padding() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchBarOrgPreviewEmpty16Padding(),
        onUIAction = {
            (UIAction(actionKey = generateSearchBarOrgPreviewEmpty16Padding().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchBarOrgPreview_NoButton() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchBarOrgPreviewNoButton(),
        onUIAction = {
            (UIAction(actionKey = generateSearchBarOrgPreviewNoButton().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_With_Text() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchInputMlcPreviewEditableWithText(),
        onUIAction = {
            (UIAction(actionKey = generateSearchInputMlcPreviewEditableWithText().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Label() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchInputMlcPreviewEditableIconLabel(),
        onUIAction = {
            (UIAction(actionKey = generateSearchInputMlcPreviewEditableIconLabel().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Label_Counter() {
    SearchBarOrg(
        modifier = Modifier,
        data = generateSearchInputMlcPreviewEditableIconLabelCounter(),
        onUIAction = {
            (UIAction(actionKey = generateSearchInputMlcPreviewEditableIconLabelCounter().actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Counter() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            badge = BadgeCounterAtmData(10),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Counter_Disabled() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            topPaddingMode = TopPaddingMode.NONE,
            sidePaddingMode = SidePaddingMode.NONE,
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            badge = BadgeCounterAtmData(1),
            interactionState = UIState.Interaction.Disabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

enum class ModeSearchInput(val value: Int) {
    EDITABLE(0), BUTTON(1)
}
