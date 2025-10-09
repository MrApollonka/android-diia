package ua.gov.diia.context_menu.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.ContextMenuOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class ContextMenuDVM @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contextMenuFields = savedStateHandle.get<Array<ContextMenuField>>("items")?.toList()

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    private val _contextMenuOrgData = MutableStateFlow<ContextMenuOrgData?>(null)
    val contextMenuOrgData = _contextMenuOrgData.asStateFlow()

    init {
        _contextMenuOrgData.update {
            ContextMenuOrgData(
                items = contextMenuFields
                    ?.map { item ->
                        ListItemMlcData(
                            label = item.getDisplayName().toDynamicString(),
                            action = DataActionWrapper(
                                type = item.getActionType(),
                                subtype = item.getSubType()
                            )
                        )
                    }
                    .orEmpty()
            )
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.action?.type ?: event.actionKey) {
            ActionsConst.CONTEXT_MENU_CLOSE -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            else -> {
                val actionType = event.action?.type
                val subType = event.action?.subtype
                val contextMenuField =
                    if (actionType != null && subType == null) {
                        contextMenuFields?.find { it.getActionType() == actionType }
                    } else if (actionType != null) {
                        contextMenuFields?.find {
                            it.getActionType() == (event.action as DataActionWrapper).type && it.getSubType() == (event.action as DataActionWrapper).subtype
                        }
                    } else {
                        null
                    }
                contextMenuField?.let {
                    _navigation.tryEmit(ContextMenuNavigation.NavigateByAction(action = it))
                }

            }
        }
    }

    sealed class ContextMenuNavigation : NavigationPath {
        data class NavigateByAction(val action: ContextMenuField) : ContextMenuNavigation()
    }

}