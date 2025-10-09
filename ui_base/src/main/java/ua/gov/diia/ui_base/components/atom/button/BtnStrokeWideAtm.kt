package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeWideAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.subatomic.loader.GraySpinnerLoaderSubAtm
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnStrokeWideAtm(
    modifier: Modifier = Modifier,
    data: BtnStrokeWideAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val isLoading = remember {
        mutableStateOf(data.id == progressIndicator.first && progressIndicator.second)
    }

    LaunchedEffect(
        key1 = data.id == progressIndicator.first,
        key2 = progressIndicator.second
    ) {
        isLoading.value = data.id == progressIndicator.first && progressIndicator.second
    }

    Button(
        modifier = modifier
            .padding(top = 16.dp)
            .height(48.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString().orEmpty()),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 2.dp,
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            }
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            if (!isLoading.value) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
        }
    ) {
        AnimatedVisibility(visible = isLoading.value) {
            GraySpinnerLoaderSubAtm()
        }
        if (!isLoading.value) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = data.title.asString(),
                color = if (data.interactionState == UIState.Interaction.Disabled) {
                    BlackAlpha10
                } else {
                    Black
                },
                style = DiiaTextStyle.t1BigText
            )
        }
    }
}

data class BtnStrokeWideAtmData(
    val actionKey: String = UIActionKeysCompose.BTN_STROKE_WIDE_ATM,
    val componentId: UiText?,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper?
) : UIElementData {

    fun changeStateByValidation(state: UIState.Interaction): BtnStrokeWideAtmData {
        return this.copy(
            interactionState = state
        )
    }
}

fun BtnStrokeWideAtm.toUIModel(
    id: String = if (componentId.isNullOrBlank()) UIActionKeysCompose.BTN_STROKE_WIDE_ATM else componentId.orEmpty()
): BtnStrokeWideAtmData {
    return BtnStrokeWideAtmData(
        componentId = componentId.orEmpty().toDynamicString(),
        id = id,
        title = label.toDynamicString(),
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
        action = action?.toDataActionWrapper()
    )
}

@Composable
@Preview
fun BtnStrokeWideAtmPreview() {
    Column {
        BtnStrokeWideAtm(
            data = BtnStrokeWideAtmData(
                componentId = null,
                id = "",
                title = "Label".toDynamicString(),
                interactionState = UIState.Interaction.Enabled,
                action = null
            )
        ) {
            /* no-op */
        }
        BtnStrokeWideAtm(
            data = BtnStrokeWideAtmData(
                componentId = null,
                id = "",
                title = "Label".toDynamicString(),
                interactionState = UIState.Interaction.Disabled,
                action = null
            )
        ) {
            /* no-op */
        }
        BtnStrokeWideAtm(
            data = BtnStrokeWideAtmData(
                componentId = null,
                id = "id",
                title = "Label".toDynamicString(),
                interactionState = UIState.Interaction.Enabled,
                action = null
            ),
            progressIndicator = Pair("id", true)
        ) {
            /* no-op */
        }
    }
}