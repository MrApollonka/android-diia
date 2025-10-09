package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent

@Composable
fun BtnStrokeLargeAtm(
    modifier: Modifier = Modifier,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    data: BtnStrokeLargeAtmData,
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
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Transparent,
            disabledContainerColor = Transparent
        ),
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 24.dp
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
                        data = data.id
                    )
                )
            }
        }
    ) {
        AnimatedVisibility(
            visible = isLoading.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoaderCircularEclipse23Subatomic(
                modifier = Modifier
                    .size(16.dp)
            )
        }
        if (!isLoading.value) {
            Text(
                text = data.title.asString(),
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha10
                    UIState.Interaction.Enabled -> Black
                },
                style = DiiaTextStyle.h4ExtraSmallHeading,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class BtnStrokeLargeAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
) : UIElementData

@Preview
@Composable
fun BtnStrokeLargeAtmEnabledPreview() {
    val buttonStateEnabled = BtnStrokeLargeAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeLargeAtm(
        data = buttonStateEnabled,
        onUIAction = {
            /* no-op */
        }
    )
}

@Preview
@Composable
fun BtnStrokeLargeAtmDisabledPreview() {
    val buttonStateDisabled = BtnStrokeLargeAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnStrokeLargeAtm(
        data = buttonStateDisabled,
        onUIAction = {
            /* no-op */
        }
    )
}

@Preview
@Composable
fun BtnStrokeLargeAtmLoadingPreview() {
    val buttonStateLoading = BtnStrokeLargeAtmData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeLargeAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true),
        onUIAction = {
            /* no-op */
        }
    )
}