package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.CONTEXT_MENU_CLOSE
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.theme.BlackAlpha7
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ContextMenuOrg(
    modifier: Modifier = Modifier,
    data: ContextMenuOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = modifier
                .background(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                )
                .verticalScroll(rememberScrollState())
                .weight(
                    weight = 1f,
                    fill = false
                )
        ) {
            data.items.forEachIndexed { index, item ->
                ListItemMlc(
                    data = item,
                    onUIAction = onUIAction,
                )
                if (index != data.items.size - 1) {
                    DividerSlimAtom(color = BlackAlpha7)
                }
            }
        }
        BtnWhiteLargeAtm(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight(),
            data = BtnWhiteLargeAtmData(
                title = UiText.StringResource(R.string.close),
                id = CONTEXT_MENU_CLOSE,
                interactionState = UIState.Interaction.Enabled,
                action = DataActionWrapper(
                    type = CONTEXT_MENU_CLOSE
                )
            ),
            onUIAction = onUIAction
        )
    }
}

data class ContextMenuOrgData(
    val items: List<ListItemMlcData>
) : UIElementData