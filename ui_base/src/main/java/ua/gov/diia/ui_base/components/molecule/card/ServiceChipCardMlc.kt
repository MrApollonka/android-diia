package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.ServiceChipCardMlc
import ua.gov.diia.ui_base.components.atom.status.SquareChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.SquareChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50
import ua.gov.diia.ui_base.helper.ServiceCardResourceHelper

@Composable
fun ServiceChipCardMlc(
    modifier: Modifier = Modifier,
    data: ServiceChipCardMlcData,
    onUIAction: (UIAction) -> Unit,
    serviceCardResourceHelper: ServiceCardResourceHelper
) {
    Column(
        modifier = modifier
            .height(138.dp)
            .background(
                color = WhiteAlpha50,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
            .padding(16.dp)
    ) {
        Image(
            modifier = Modifier
                .size(32.dp),
            painter = painterResource(
                serviceCardResourceHelper.getIconResourceId(data.icon.code)
            ),
            contentDescription = stringResource(
                serviceCardResourceHelper.geIconContentDescription(data.icon.code)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = data.label,
                style = DiiaTextStyle.t3TextBody,
                color = Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            data.squareChipStatusAtmData?.let { lSquareChipStatusAtmData ->
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                SquareChipStatusAtm(
                    modifier = Modifier
                        .align(Alignment.Start),
                    data = lSquareChipStatusAtmData
                )
            }
        }
    }
}

data class ServiceChipCardMlcData(
    val actionKey: String = UIActionKeysCompose.PS_ITEM_CLICK,
    val id: String? = "",
    val label: String,
    val icon: UiIcon.DrawableResource,
    val squareChipStatusAtmData: SquareChipStatusAtmData?
) : UIElementData

fun ServiceChipCardMlc.toUIModel(): ServiceChipCardMlcData {
    return ServiceChipCardMlcData(
        actionKey = this.action?.type ?: UIActionKeysCompose.PS_ITEM_CLICK,
        label = this.label,
        icon = UiIcon.DrawableResource(code = this.icon),
        squareChipStatusAtmData = squareChipStatusAtm?.toUiModel()
    )
}