package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Honeydew
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun BtnNumAtm(
    modifier: Modifier = Modifier,
    data: BtnNumAtmData,
    onUIAction: (UIAction) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isPressed) Black else Honeydew)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            text = data.number.toString(),
            style = DiiaTextStyle.numberText,
            color = if (isPressed) White else Black
        )
    }
}

data class BtnNumAtmData(
    val actionKey: String = UIActionKeysCompose.NUM_BUTTON,
    val id: String = "",
    val number: Int
) : UIElementData

@Composable
@Preview
fun BtnNumAtmPreview() {
    Box(
        modifier = Modifier
            .size(100.dp)
    ) {
        BtnNumAtm(
            data = BtnNumAtmData(number = 1),
            onUIAction = {
                /* no-op */
            }
        )
    }
}