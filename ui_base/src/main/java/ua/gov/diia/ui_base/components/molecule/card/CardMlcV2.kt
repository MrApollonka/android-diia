package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.CardMlcV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconUrlAtm
import ua.gov.diia.ui_base.components.atom.icon.IconUrlAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun CardMlcV2(
    modifier: Modifier = Modifier,
    data: CardMlcV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 24.dp)
            )
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString().orEmpty())
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    data.chip?.let {
                        ChipStatusAtm(
                            modifier = Modifier,
                            data = data.chip
                        )
                    }
                    data.rightLabel?.let {
                        Spacer(Modifier.weight(1f))
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = data.rightLabel.asString(),
                            style = DiiaTextStyle.t2TextDescription,
                            color = BlackAlpha50
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .conditional(data.chip != null) {
                            padding(top = 16.dp)
                        },
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    data.iconUrl?.let {
                        IconUrlAtm(
                            modifier = Modifier
                                .semantics {
                                    hideFromAccessibility()
                                },
                            data = it
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        data.label?.let {
                            Text(
                                modifier = Modifier,
                                text = data.label.asString(),
                                style = DiiaTextStyle.t1BigText,
                                color = Black
                            )
                        }
                        data.descriptions?.forEach {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                text = it,
                                style = DiiaTextStyle.t2TextDescription,
                                color = BlackAlpha30
                            )
                        }
                        data.rows?.forEach {
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                overflow = TextOverflow.Ellipsis,
                                text = it,
                                style = DiiaTextStyle.t2TextDescription,
                                color = BlackAlpha30
                            )
                        }
                        data.chips?.let {
                            FlowRow(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)

                            ) {
                                data.chips.forEach {
                                    ChipStatusAtm(
                                        data = it
                                    )
                                }
                            }
                        }
                    }
                    val currentSmallIcon =
                        data.smallIconStates?.get(data.smallIconCurrentState) ?: data.smallIconAtm
                    currentSmallIcon?.let { lSmallIconAtmData ->
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .align(Alignment.Bottom)
                                .noRippleClickable {
                                    onUIAction(currentSmallIcon.action())
                                }
                        ) {
                            SmallIconAtm(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd),
                                data = lSmallIconAtmData,
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CardMlcV2Data(
    val actionKey: String = UIActionKeysCompose.CARD_MLC_V2,
    val componentId: UiText? = null,
    override val id: String,
    val chip: ChipStatusAtmData? = null,
    val label: UiText? = null,
    val chips: List<ChipStatusAtmData>? = null,
    val smallIconAtm: SmallIconAtmData? = null,
    val smallIconCurrentState: String? = null,
    val smallIconStates: Map<String, SmallIconAtmData>? = null,
    val iconUrl: IconUrlAtmData? = null,
    val descriptions: List<String>? = null,
    val rows: List<String>? = null,
    val rightLabel: UiText? = null,
    val action: DataActionWrapper? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : SimplePagination {

    fun updateSmallIconCurrentState(newState: String): CardMlcV2Data {
        return this.copy(smallIconCurrentState = newState)
    }
}

fun CardMlcV2.toUIModel(): CardMlcV2Data {
    val result = mutableListOf<ChipStatusAtmData>()
    chips?.let { ch ->
        ch.forEach { item ->
            val chipStatusAtmData = item.chipStatusAtm?.toUiModel()
            chipStatusAtmData?.let { lChipStatusAtmData ->
                result.add(lChipStatusAtmData)
            }
        }
    }
    return CardMlcV2Data(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.componentId ?: "",
        chip = this.chipStatusAtm?.toUiModel(),
        chips = if (result.isEmpty()) null else result,
        smallIconAtm = this.smallIconAtm?.toUiModel(),
        smallIconCurrentState = this.smallIconAtmWithStates?.currentState,
        smallIconStates = buildMap {
            smallIconAtmWithStates?.states?.forEach {
                put(it.name, it.icon.toUiModel())
            }
        },
        label = this.label?.let { UiText.DynamicString(it) },
        iconUrl = this.iconUrlAtm?.toUiModel(),
        action = action?.toDataActionWrapper(),
        descriptions = this.descriptions,
        rows = this.rows,
        rightLabel = this.rightLabel?.let { UiText.DynamicString(it) },
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
    )
}

@Composable
@Preview
fun CardMlcV2Preview_FullState() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1", "description2"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        smallIconStates = mapOf(
            "1" to SmallIconAtmData(id = "1122112", code = DiiaResourceIcon.PLACEHOLDER.code),
            "2" to SmallIconAtmData(id = "22212", code = DiiaResourceIcon.RING.code),
            "3" to SmallIconAtmData(id = "2221", code = DiiaResourceIcon.HEART.code),
            "4" to SmallIconAtmData(id = "123", code = DiiaResourceIcon.RATING.code),
        ),
        smallIconCurrentState = "4",
        chips = listOf(chipWhite, chipWhite, chipWhite, chipWhite, chipWhite, chipWhite),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_LabelRight() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1", "description2"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        chips = listOf(chipWhite, chipWhite, chipWhite),
        paddingTop = null,
        paddingHorizontal = null,
        rightLabel = UiText.DynamicString("right label")
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WithoutStatus() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        chips = listOf(chipWhite, chipWhite, chipWhite, chipWhite),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WhithoutChips() {
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_Rows() {
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconUrl = null,
        rows = listOf("row1", "row2"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WithoutIcon() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        descriptions = listOf("description1", "description2"),
        chips = listOf(chipWhite, chipWhite, chipWhite),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_LabelChips() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        chips = listOf(chipWhite, chipWhite, chipWhite),
        paddingTop = null,
        paddingHorizontal = null
    )
    CardMlcV2(data = state) {
    }
}