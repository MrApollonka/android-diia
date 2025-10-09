package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcV2
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcV2Data
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlc
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrg
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData

@Composable
fun ToolbarRootCollapseContainer(
    modifier: Modifier = Modifier,
    toolbar: SnapshotStateList<UIElementData>,
    lazyListState: LazyListState,
    onEvent: (UIAction) -> Unit
) {
    var alpha by remember { mutableFloatStateOf(0F) }
    Box {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                toolbar.forEachIndexed { index, item ->
                    when (item) {
                        is NavigationPanelMlcV2Data -> {
                            NavigationPanelMlcV2(
                                modifier = Modifier.fillMaxWidth(),
                                data = item,
                                lazyListState = lazyListState,
                                alphaCallback = { newAlpha ->
                                    alpha = newAlpha
                                },
                                onUIAction = onEvent
                            )
                        }

                        is SearchInputMlcData -> {
                            SearchInputMlc(
                                data = item,
                                onUIAction = onEvent
                            )
                        }

                        is SearchBarOrgData -> {
                            SearchBarOrg(
                                data = item,
                                onUIAction = onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}