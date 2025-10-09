package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.SmallNotificationMlc
import ua.gov.diia.ui_base.components.atom.icon.SmallIconUrlAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconUrlAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUIModel
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun SmallNotificationMlc(
    modifier: Modifier = Modifier,
    data: SmallNotificationMlcData,
    clickable: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .conditional(clickable) {
                noRippleClickable {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
                }
            }
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .semantics {
                onClick(label = null, action = null)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    data.smallIconUrlAtmData?.let { lSmallIconUrlAtmData ->
                        SmallIconUrlAtm(
                            data = lSmallIconUrlAtmData
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = data.label.asString(),
                        color = Black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = DiiaTextStyle.t2TextDescription
                    )
                }
                data.chipStatusAtmData?.let { lChipStatusAtmData ->
                    ChipStatusAtm(
                        data = lChipStatusAtmData
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = data.text.asString(),
                color = Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = DiiaTextStyle.t1BigText
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
        }
    }
}

data class SmallNotificationMlcData(
    val actionKey: String = UIActionKeysCompose.SMALL_NOTIFICATION_MLC,
    override val id: String? = "",
    val text: UiText,
    val label: UiText,
    val chipStatusAtmData: ChipStatusAtmData? = null,
    val smallIconUrlAtmData: SmallIconUrlAtmData? = null,
    val action: DataActionWrapper? = null
) : SimpleCarouselCard

fun SmallNotificationMlc.toUIModel() = SmallNotificationMlcData(
    id = this.id,
    text = text.toDynamicString(),
    label = label.toDynamicString(),
    chipStatusAtmData = chipStatusAtm?.toUiModel(),
    smallIconUrlAtmData = this.smallIconUrlAtm?.toUIModel(),
    action = action?.toDataActionWrapper()
)

@Preview
@Composable
fun SmallNotificationMlcPreview() {
    val loremIpsum = LoremIpsum(25).values.joinToString().toDynamicString()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SmallNotificationMlc(
            data = SmallNotificationMlcData(
                label = loremIpsum,
                text = loremIpsum,
            ),
            onUIAction = { /* no-op */ }
        )
        SmallNotificationMlc(
            data = SmallNotificationMlcData(
                label = loremIpsum,
                text = loremIpsum,
                smallIconUrlAtmData = SmallIconUrlAtmData(
                    componentId = null,
                    url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
                    accessibilityDescription = "SmallIconUrlAtm",
                    action = null
                )
            ),
            onUIAction = { /* no-op */ }
        )
        SmallNotificationMlc(
            data = SmallNotificationMlcData(
                label = loremIpsum,
                text = loremIpsum,
                chipStatusAtmData = ChipStatusAtmData(
                    type = StatusChipType.PENDING,
                    title = "Label"
                )
            ),
            onUIAction = { /* no-op */ }
        )
        SmallNotificationMlc(
            data = SmallNotificationMlcData(
                label = loremIpsum,
                text = loremIpsum,
                chipStatusAtmData = ChipStatusAtmData(
                    type = StatusChipType.PENDING,
                    title = "Label"
                ),
                smallIconUrlAtmData = SmallIconUrlAtmData(
                    componentId = null,
                    url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
                    accessibilityDescription = "SmallIconUrlAtm",
                    action = null
                )
            ),
            onUIAction = { /* no-op */ }
        )
    }
}