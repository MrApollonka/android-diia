package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.button.OutlineButtonOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.molecule.button.OutlineButtonMlc
import ua.gov.diia.ui_base.components.molecule.button.OutlineButtonMlcData
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun OutlineButtonOrg(
    modifier: Modifier = Modifier,
    data: OutlineButtonOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(
                start = data.sidePaddingMode.toDp(defaultPadding = 16.dp),
                top = data.topPaddingMode.toDp(defaultPadding = 8.dp),
                end = data.sidePaddingMode.toDp(defaultPadding = 16.dp)
            )
            .border(width = 2.dp, color = White, shape = RoundedCornerShape(16.dp))
    ) {
        data.itemsList.forEachIndexed { index, item ->
            OutlineButtonMlc(data = item, onUIAction = onUIAction, progressIndicator = progressIndicator)
            if (index != data.itemsList.size - 1) {
                DividerSlimAtom(modifier = Modifier.padding(horizontal = 16.dp), color = White)
            }
        }
    }
}

data class OutlineButtonOrgData(
    val componentId: UiText? = null,
    val itemsList: List<OutlineButtonMlcData>,
    val topPaddingMode: TopPaddingMode? = null,
    val sidePaddingMode: SidePaddingMode? = null
) : UIElementData

fun generateOutlineButtonOrgMockData(): OutlineButtonOrgData {
    return OutlineButtonOrgData(
        componentId = "id".toDynamicString(),
        SnapshotStateList<OutlineButtonMlcData>().apply {
            add(
                OutlineButtonMlcData(
                    id = "id",
                    label = "Зареєструвати авто".toDynamicString(),
                    iconLeft = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.PLACEHOLDER.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
            add(
                OutlineButtonMlcData(
                    id = "id",
                    label = "Зареєструвати авто".toDynamicString(),
                    iconLeft = SmallIconAtmData(
                        id = "1",
                        code = DiiaResourceIcon.PLACEHOLDER.code,
                        accessibilityDescription = "Button"
                    ),
                )
            )
        })
}

@Composable
@Preview
fun OutlineButtonOrgPreview() {
    val state = generateOutlineButtonOrgMockData()
    OutlineButtonOrg(data = state) {
    }
}

fun OutlineButtonOrg.toUIModel(): OutlineButtonOrgData {
    val data = this
    return OutlineButtonOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        itemsList = mutableListOf<OutlineButtonMlcData>().apply {
            data.items?.forEachIndexed { index, item ->
                item.outlineButtonMlc?.let {
                    add(
                        it.toUIModel(
                            item.outlineButtonMlc?.componentId ?: index.toString()
                        )
                    )
                }
            }
        }
    )
}