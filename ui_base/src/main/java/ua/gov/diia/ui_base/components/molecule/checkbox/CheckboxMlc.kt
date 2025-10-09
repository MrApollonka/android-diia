package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackAlpha54
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxMlc(
    modifier: Modifier = Modifier,
    data: CheckboxMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                start = data.sidePaddingMode.toDp(defaultPadding = 16.dp),
                top = data.topPaddingMode.toDp(defaultPadding = 8.dp),
                end = data.sidePaddingMode.toDp(defaultPadding = 16.dp)
            )
            .conditional(data.interactionState == UIState.Interaction.Enabled) {
                noRippleClickable {
                    onUIAction(data.action())
                }
            },
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = when (data.interactionState) {
                        UIState.Interaction.Disabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> BlackAlpha30
                                UIState.Selection.Unselected -> Transparent
                            }
                        }

                        UIState.Interaction.Enabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> Black
                                UIState.Selection.Unselected -> Transparent
                            }
                        }
                    },
                    shape = RoundedCornerShape(4.dp)
                )
                .conditional(data.selectionState == UIState.Selection.Unselected) {
                    border(
                        color = when (data.interactionState) {
                            UIState.Interaction.Disabled -> BlackAlpha30
                            UIState.Interaction.Enabled -> Black
                        },
                        width = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
                }
                .size(20.dp)
                .testTag(data.componentId.orEmpty()),
            contentAlignment = Alignment.Center
        ) {
            if (data.selectionState == UIState.Selection.Selected) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    painter = painterResource(R.drawable.diia_check),
                    contentDescription = null,
                    tint = White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                style = DiiaTextStyle.t1BigText,
                text = data.label,
                color = when (data.interactionState) {
                    UIState.Interaction.Enabled -> Black

                    UIState.Interaction.Disabled -> BlackAlpha30
                }
            )
            data.description?.let { lDescription ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = DiiaTextStyle.t2TextDescription,
                    text = lDescription,
                    color = when (data.interactionState) {
                        UIState.Interaction.Enabled -> BlackAlpha54

                        UIState.Interaction.Disabled -> BlackAlpha30
                    }
                )
            }
        }
    }
}

data class CheckboxMlcData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_MLC,
    val componentId: String? = null,
    val label: String,
    val description: String? = null,
    val interactionState: UIState.Interaction,
    val selectionState: UIState.Selection,
    val topPaddingMode: TopPaddingMode? = null,
    val sidePaddingMode: SidePaddingMode? = null
) : UIElementData {

    fun onCheckboxClick(): CheckboxMlcData {
        return this.copy(selectionState = this.selectionState.reverse())
    }

    fun updateInteractionState(newState: UIState.Interaction): CheckboxMlcData {
        return this.copy(interactionState = newState)
    }

    fun action(): UIAction {
        return UIAction(
            actionKey = this.actionKey,
            data = this.componentId,
            action = DataActionWrapper(
                type = this.actionKey,
                resource = this.componentId
            ),
            states = listOf(this.selectionState)
        )
    }

}

fun generateMockCheckboxMlcData() = CheckboxMlcData(
    label = "Label",
    description = "Description",
    interactionState = UIState.Interaction.Enabled,
    selectionState = UIState.Selection.Unselected,
    topPaddingMode = TopPaddingMode.NONE,
    sidePaddingMode = SidePaddingMode.NONE
)

@Preview
@Composable
private fun CheckboxMlcPreview() {
    CheckboxMlc(
        data = generateMockCheckboxMlcData(),
        onUIAction = {
            /* no-op */
        }
    )
}