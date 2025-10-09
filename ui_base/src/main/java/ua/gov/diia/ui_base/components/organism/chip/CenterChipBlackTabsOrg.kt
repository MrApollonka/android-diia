package ua.gov.diia.ui_base.components.organism.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.molecule.chip.ChipBlackMlcData
import ua.gov.diia.core.models.common_compose.org.chip.CenterChipBlackTabsOrg
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.chip.ChipBlackMlc
import ua.gov.diia.ui_base.components.molecule.chip.toUiModel


@Composable
fun CenterChipBlackTabsOrg(
    modifier: Modifier = Modifier,
    data: CenterChipBlackTabsOrgData,
    onUIAction: (UIAction) -> Unit
) {
    var selectedCode by remember { mutableStateOf(data.preselectedCode) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(tag = data.componentId),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        data.items.forEach { chip ->
            ChipBlackMlc(
                modifier = Modifier.weight(data.items.size * 0.1f),
                data = chip.copy(
                    selectionState = if (chip.code == selectedCode) {
                        UIState.Selection.Selected
                    } else {
                        UIState.Selection.Unselected
                    }
                ),
                onUIAction = {
                    if (selectedCode != chip.code) {
                        selectedCode = chip.code
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = chip.code,
                                optionalId = data.componentId
                            )
                        )
                    }
                }
            )
        }
    }
}

data class CenterChipBlackTabsOrgData(
    val actionKey: String = UIActionKeysCompose.CENTERED_CHIP_BLACK_TABS_ORG,
    val componentId: String = UIActionKeysCompose.CENTERED_CHIP_BLACK_TABS_ORG,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val items: List<ChipBlackMlcData>,
    val preselectedCode: String
) : UIElementData


fun CenterChipBlackTabsOrg.toUiModel(): CenterChipBlackTabsOrgData {
    return CenterChipBlackTabsOrgData(
        componentId = componentId,
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        items = items.map { it.chipBlackMlc.toUiModel() },
        preselectedCode = preselectedCode
    )
}

fun generateCenterChipBlackTabOrgMockData(): CenterChipBlackTabsOrgData {
    val list = buildList {
        add(
            ChipBlackMlcData(
                label = UiText.DynamicString("label"),
                code = "1",
                active = true,
                selectionState = UIState.Selection.Selected
            )
        )
        repeat(3) {
            add(
                ChipBlackMlcData(
                    label = UiText.DynamicString("label"),
                    code = "2",
                    active = true,
                    selectionState = UIState.Selection.Unselected
                )
            )
        }

    }
    return CenterChipBlackTabsOrgData(
        items = list,
        preselectedCode = "2"
    )
}

fun generateCenterChipBlackTab_Two_OrgMockData(preselectedCode: String = "2"): CenterChipBlackTabsOrgData {
    val list = buildList {
        add(
            ChipBlackMlcData(
                label = UiText.DynamicString("label"),
                code = "1",
                active = true,
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipBlackMlcData(
                label = UiText.DynamicString("label"),
                code = "2",
                active = true,
                selectionState = UIState.Selection.Unselected
            )
        )
    }
    return CenterChipBlackTabsOrgData(
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        items = list,
        preselectedCode = preselectedCode
    )
}

@Composable
@Preview
fun CenterChipBlackTabsOrg_Preview() {
    CenterChipBlackTabsOrg(
        data = generateCenterChipBlackTabOrgMockData()
    ) {}
}

@Composable
@Preview
fun CenterChipBlackTabsOrg_Two_Preview() {
    CenterChipBlackTabsOrg(
        data = generateCenterChipBlackTab_Two_OrgMockData()
    ) {}
}


@Composable
@Preview
fun CenterChipBlackTabsOrg_One_Preview() {
    val list = buildList<ChipBlackMlcData> {
        add(
            ChipBlackMlcData(
                label = UiText.DynamicString("label"),
                code = "1",
                active = true,
                selectionState = UIState.Selection.Selected
            )
        )
    }
    CenterChipBlackTabsOrg(
        data = CenterChipBlackTabsOrgData(
            items = list,
            preselectedCode = "2"
        )
    ) {}
}



