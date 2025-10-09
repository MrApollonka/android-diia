package ua.gov.diia.verification.ui.method_selection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.LIST_ITEM_MLC
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.ContextMenuOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.verification.model.VerificationMethodsView
import javax.inject.Inject

@HiltViewModel
internal class VerificationMethodSelectionVM @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val verificationMethods = savedStateHandle.get<VerificationMethodsView>("data")

    private val _contextMenuOrgData = MutableStateFlow<ContextMenuOrgData?>(null)
    val contextMenuOrgData = _contextMenuOrgData.asStateFlow()

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    init {
        _contextMenuOrgData.update {
            val items = mutableListOf<ListItemMlcData>()
            items.add(
                ListItemMlcData(
                    label = verificationMethods?.title.toDynamicString()
                )
            )
            items.addAll(
                elements = verificationMethods?.methods
                    ?.map { item ->
                        ListItemMlcData(
                            label = UiText.StringResource(item.titleRes),
                            logoLeft = UiIcon.DrawableResInt(item.iconRes),
                            action = DataActionWrapper(
                                type = item.code
                            )
                        )
                    }
                    .orEmpty()
            )
            ContextMenuOrgData(
                items = items
            )
        }
    }

    fun onUIAction(action: UIAction) {
        when (action.actionKey) {
            LIST_ITEM_MLC -> {
                val verificationMethodCode = action.action?.type ?: return
                _navigation.tryEmit(Navigation.SelectedVerificationMethod(verificationMethodCode))
            }
        }

        when (action.action?.type) {
            ActionsConst.CONTEXT_MENU_CLOSE -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }
        }
    }

    sealed interface Navigation : NavigationPath {
        data class SelectedVerificationMethod(val code: String) : Navigation
    }

}