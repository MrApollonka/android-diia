package ua.gov.diia.ps_criminal_cert.ui.steps.confirm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common_compose.general.DiiaResponse
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_CONFIRMATION_ACTION_CONFIRM
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_CONFIRMATION_ACTION_LINK
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepConfirmVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val withContextMenu: WithContextMenu<ContextMenuField>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val navigationHelper: PSNavigationHelper,
    private val withRatingDialog: WithRatingDialogOnFlow
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    PSNavigationHelper by navigationHelper,
    WithContextMenu<ContextMenuField> by withContextMenu,
    WithRatingDialogOnFlow by withRatingDialog {

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL)
    private val _contentLoaded = MutableStateFlow(false)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key ->
            key to value
        }

    private val _topGroupData = mutableStateListOf<UIElementData>()
    val topGroupData: SnapshotStateList<UIElementData> = _topGroupData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _bottomData = mutableStateListOf<UIElementData>()
    val bottomData: SnapshotStateList<UIElementData> = _bottomData

    private val _navigation =
        MutableSharedFlow<NavigationPath>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val navigation = _navigation.asSharedFlow()

    fun setNavBarTitle(navBarTitle: String?) {
        this.navBarTitle = navBarTitle
    }

    private var navBarTitle: String? = null
    private var applicationId: String? = null

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                _bodyData.findAndChangeFirstByInstance<CheckboxBtnOrgData> {
                    it.onOptionsCheckChanged(event.data)
                }
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_CONFIRMATION_ACTION_CONFIRM -> {
                confirmApplication(action.resource ?: applicationId)
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
            }

            SCREEN_CONFIRMATION_ACTION_LINK -> {
                _navigation.tryEmit(ConfirmationNavigation.ToWebView(action.resource))
            }
        }
    }

    fun getScreenContent(id: String) {
        applicationId = id
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value =
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
            })
        {
            api.getCriminalCertConfirmation(id).let { response ->
                response.template?.let { showTemplateDialog(it) }
                if (response.template == null) {
                    response.topGroup?.let { setContextMenu(it.getEllipseMenu()?.toTypedArray()) }
                    showScreenContent(response)
                }
            }
        }
    }

    private fun showScreenContent(content: DiiaResponse) {
        content.topGroup?.forEach { item ->
            item.mapToComposeTopData(!withContextMenu.getMenu().isNullOrEmpty())?.let {
                _topGroupData.addAllIfNotNull(it)
            }
        }

        content.body?.forEach { item ->
            item.mapToComposeBodyData()?.let {
                _bodyData.addAllIfNotNull(it)
            }
        }
        content.bottomGroup?.forEach { item ->
            item.mapToComposeBottomData()?.let {
                _bottomData.addAllIfNotNull(it)
            }
        }
    }

    private fun confirmApplication(id: String?) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.CONFIRM_BUTTON_ID
            }) {
            id?.let {
                api.confirmApplication(it).let { response ->
                    if (response.template != null) {
                        showTemplateDialog(response.template)
                    }
                }
            }
        }
    }

    fun cancelApplication(force: Boolean) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.CANCEL_BUTTON_ID
            }
        ) {
            if (!applicationId.isNullOrEmpty()) {
                api.cancelCriminalCertApplication(
                    applicationId = applicationId ?: return@executeActionOnFlow,
                    force = force,
                ).let {
                    it.template?.let {
                        showTemplateDialog(it)
                    }
                }
            }
        }
    }

    fun getRatingForm() {
        getRating(
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(
            ratingRequest = ratingRequest,
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

    sealed class ConfirmationNavigation : NavigationPath {
        data class ToWebView(val url: String?) : ConfirmationNavigation()
    }
}