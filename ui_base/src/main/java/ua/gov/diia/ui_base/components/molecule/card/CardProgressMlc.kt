package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.ExtraLargeIconUrlAtm
import ua.gov.diia.ui_base.components.atom.icon.ExtraLargeIconUrlAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.generateMockExtraLargeIconUrlAtmData
import ua.gov.diia.ui_base.components.atom.progress.ProgressBarAtm
import ua.gov.diia.ui_base.components.atom.progress.ProgressBarAtmData
import ua.gov.diia.ui_base.components.atom.progress.generateMockProgressBarAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CARD_PROGRESS_MLC
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha54
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CardProgressMlc(
    modifier: Modifier = Modifier,
    data: CardProgressMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .then(
                if (data.action == null && context.isTalkBackEnabled()) {
                    Modifier.semantics(mergeDescendants = true) { }
                } else {
                    Modifier.noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = data.componentId,
                                action = data.action
                            )
                        )
                    }
                }
            )
            .padding(16.dp)
            .testTag(data.componentId.orEmpty()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (data.chipStatusAtmData != null || data.rightLabel != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                data.chipStatusAtmData?.let { lChipStatusAtmData ->
                    ChipStatusAtm(
                        modifier = Modifier
                            .align(Alignment.TopStart),
                        data = lChipStatusAtmData
                    )
                }
                data.rightLabel?.let { lRightLabel ->
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        style = DiiaTextStyle.t2TextDescription,
                        text = lRightLabel,
                        color = BlackAlpha54
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data.leftBigImage?.let { lLeftBigImage ->
                ExtraLargeIconUrlAtm(
                    data = lLeftBigImage,
                    onUIAction = onUIAction
                )
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Text(
                    style = DiiaTextStyle.t1BigText,
                    text = data.label,
                    color = Black
                )
                data.description?.let { lDescription ->
                    Text(
                        style = DiiaTextStyle.t2TextDescription,
                        text = lDescription,
                        color = BlackAlpha54
                    )
                }
            }
            data.iconRight?.let { lIconRight ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .noRippleClickable {
                            onUIAction(lIconRight.action())
                        }
                ) {
                    SmallIconAtm(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        data = lIconRight,
                        onUIAction = onUIAction
                    )
                }
            }
        }
        data.progressBarAtmData?.let { lProgressBarAtmData ->
            ProgressBarAtm(
                modifier = Modifier
                    .fillMaxWidth(),
                data = lProgressBarAtmData
            )
        }
    }
}

data class CardProgressMlcData(
    val actionKey: String = CARD_PROGRESS_MLC,
    val componentId: String? = null,
    val chipStatusAtmData: ChipStatusAtmData? = null,
    val rightLabel: String? = null,
    val leftBigImage: ExtraLargeIconUrlAtmData? = null,
    val label: String,
    val description: String? = null,
    val iconRight: SmallIconAtmData? = null,
    val progressBarAtmData: ProgressBarAtmData? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val action: DataActionWrapper? = null
) : UIElementData

fun generateMockCardProgressMlcData() = CardProgressMlcData(
    chipStatusAtmData = ChipStatusAtmData(
        type = StatusChipType.POSITIVE,
        title = "STATUS"
    ),
    rightLabel = "Label",
    leftBigImage = generateMockExtraLargeIconUrlAtmData(),
    label = "Label",
    description = "Description",
    iconRight = SmallIconAtmData(
        code = DiiaResourceIcon.ELLIPSE_INFO.code
    ),
    progressBarAtmData = generateMockProgressBarAtm(),
    paddingTop = null,
    paddingHorizontal = null
)

@Preview
@Composable
private fun CardProgressMlcPreview() {
    CardProgressMlc(
        data = generateMockCardProgressMlcData(),
        onUIAction = {
            /* no-op */
        }
    )
}