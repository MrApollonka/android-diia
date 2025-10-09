package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.icon.MediumIconAtm
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun MediumIconAtm(
    modifier: Modifier = Modifier,
    data: MediumIconAtmData,
    isParentActionAvailable: Boolean = false,
    onUIAction: (UIAction) -> Unit = {}
) {
    val context = LocalContext.current

    Image(
        modifier = modifier
            .size(48.dp)
            .then(
                if(!isParentActionAvailable && data.action == null && context.isTalkBackEnabled()) {
                    Modifier.focusable()
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
            .semantics {
                testTag = data.componentId ?: ""
            }
            .alpha(if (data.interactionState == UIState.Interaction.Disabled) 0.3f else 1f),
        painter = painterResource(
            id = DiiaResourceIcon.getResourceId(data.code)
        ),
        contentDescription = data.accessibilityDescription
            ?: stringResource(id = DiiaResourceIcon.getContentDescription(data.code))
    )
}

data class MediumIconAtmData(
    val actionKey: String = UIActionKeysCompose.MEDIUM_ICON_ATM,
    val id: String? = null,
    val componentId: String? = "",
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
)

fun MediumIconAtm.toUIModel() : MediumIconAtmData {
    val isEnabled = this.isEnable ?: true
    return MediumIconAtmData(
        id = this.id ?: this.componentId ?: "mediumIconAtm",
        componentId = this.componentId,
        code = this.code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper(),
        interactionState = if (isEnabled) UIState.Interaction.Enabled
            else UIState.Interaction.Disabled
    )
}

@Preview
@Composable
fun MediumIconAtmPreview() {
    val data = MediumIconAtmData(
        id = "1",
        code = DiiaResourceIcon.PRIVAT_BANK.code,
        accessibilityDescription = "Button"
    )
    MediumIconAtm(data = data)
}

@Preview
@Composable
fun MediumIconAtmPreview_disabled() {
    val data = MediumIconAtmData(
        id = "1",
        code = DiiaResourceIcon.PRIVAT_BANK.code,
        accessibilityDescription = "Button",
        interactionState = UIState.Interaction.Disabled
    )
    MediumIconAtm(data = data)
}

@Preview
@Composable
fun MediumIconAtmPreview_enabled() {
    val data = MediumIconAtmData(
        id = "1",
        code = DiiaResourceIcon.PRIVAT_BANK.code,
        accessibilityDescription = "Button",
        interactionState = UIState.Interaction.Enabled
    )
    MediumIconAtm(data = data)
}

