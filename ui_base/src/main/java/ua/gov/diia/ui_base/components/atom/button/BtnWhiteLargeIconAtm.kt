package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnWhiteLargeIconAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnWhiteLargeIconAtm(
    modifier: Modifier = Modifier,
    data: BtnWhiteLargeIconAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
            .noRippleClickable(
                enabled = data.interactionState == UIState.Interaction.Enabled,
                onClick = {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action,
                        )
                    )
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = data.label,
            tint = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.label,
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.t3TextBody,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class BtnWhiteLargeIconAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val label: String,
    val componentId: UiText? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null,
    val icon: UiIcon? = null,
) : UIElementData

fun BtnWhiteLargeIconAtm.toUIModel(id: String? = null): BtnWhiteLargeIconAtmData {
    return BtnWhiteLargeIconAtmData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        id = this.componentId ?: id ?: "",
        label = label ?: "",
        interactionState = when (this.state) {
            ButtonStates.enabled -> UIState.Interaction.Enabled
            ButtonStates.disabled -> UIState.Interaction.Disabled
            ButtonStates.invisible -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        },
        action = action?.toDataActionWrapper()
    )
}

@Preview
@Composable
private fun BtnWhiteLargeIconAtmPreview_EnabledState() {
    BtnWhiteLargeIconAtm(
        data = BtnWhiteLargeIconAtmData(
            label = LoremIpsum(1).values.joinToString(),
            id = "",
            interactionState = UIState.Interaction.Enabled
        ),
        onUIAction = {}
    )
}

@Preview
@Composable
private fun BtnWhiteLargeIconAtmPreview_DisabledState() {
    BtnWhiteLargeIconAtm(
        data = BtnWhiteLargeIconAtmData(
            label = LoremIpsum(6).values.joinToString(),
            id = "",
            interactionState = UIState.Interaction.Disabled
        ),
        onUIAction = {}
    )
}