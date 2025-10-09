package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconPlainGroupMlc
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.getLegacyProgress
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun BtnIconPlainGroupMlc(
    modifier: Modifier = Modifier,
    data: BtnIconPlainGroupMlcData,
    loader: Loader = Loader.create(),
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember {
        mutableStateOf(data)
    }

    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    LaunchedEffect(key1 = loader) {
        localData.value =
            localData.value.copy(listBtn = SnapshotStateList<BtnPlainIconAtmData>().apply {
                for (item in localData.value.listBtn) {
                    add(
                        item.copy(
                            interactionState = if (loader.isLoadingByComponent()) {
                                UIState.Interaction.Disabled
                            } else {
                                UIState.Interaction.Enabled
                            }
                        )
                    )
                }
            })
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 24.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 24.dp)
            )
            .border(
                color = PeriwinkleGray, width = 1.dp, shape = RoundedCornerShape(12.dp)
            )
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            localData.value.listBtn.forEach {
                BtnPlainIconAtm(modifier, it, loader.getLegacyProgress(), onUIAction = onUIAction)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

data class BtnIconPlainGroupMlcData(
    val componentId: UiText? = null,
    val listBtn: List<BtnPlainIconAtmData>,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

fun BtnIconPlainGroupMlc?.toUIModel(): BtnIconPlainGroupMlcData? {
    if (this == null) return null
    return BtnIconPlainGroupMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        listBtn = this.items?.map {
            it.btnPlainIconAtm.toUiModel()
        }.orEmpty(),
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode()
    )
}

enum class BtnIconPlaiGroupMockTypes {
    ONE, MANY
}

fun generateBtnIconPlanMlcMockData(type: BtnIconPlaiGroupMockTypes): BtnIconPlainGroupMlcData {
    return when (type) {
        BtnIconPlaiGroupMockTypes.MANY -> {
            val btn1 = BtnPlainIconAtmData(
                id = "123",
                label = UiText.DynamicString("label1 click here"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                interactionState = UIState.Interaction.Enabled
            )
            val btn2 = BtnPlainIconAtmData(
                id = "124",
                label = UiText.DynamicString("label2 very long name for button 2"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                interactionState = UIState.Interaction.Disabled
            )
            val btn3 = BtnPlainIconAtmData(
                id = "125",
                label = UiText.DynamicString("label3"),
                icon = null,
                interactionState = UIState.Interaction.Enabled
            )
            BtnIconPlainGroupMlcData(listBtn = listOf(btn1, btn2, btn3), paddingHorizontal = SidePaddingMode.MEDIUM)
        }

        BtnIconPlaiGroupMockTypes.ONE -> {
            val btn1 = BtnPlainIconAtmData(
                id = "123",
                label = UiText.DynamicString("label1"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.SHARE.code),
                interactionState = UIState.Interaction.Enabled
            )
            BtnIconPlainGroupMlcData(listBtn = listOf(btn1), paddingTop = TopPaddingMode.LARGE, paddingHorizontal = SidePaddingMode.MEDIUM)
        }
    }

}

@Composable
@Preview(showBackground = true)
fun BtnIconPlainGroupMlcPreview() {
    BtnIconPlainGroupMlc(
        Modifier,
        generateBtnIconPlanMlcMockData(BtnIconPlaiGroupMockTypes.MANY)
    ) {}
}

@Composable
@Preview(showBackground = true)
fun BtnIconPlainGroupMlcPreview_one() {

    BtnIconPlainGroupMlc(Modifier, generateBtnIconPlanMlcMockData(BtnIconPlaiGroupMockTypes.ONE)) {}
}