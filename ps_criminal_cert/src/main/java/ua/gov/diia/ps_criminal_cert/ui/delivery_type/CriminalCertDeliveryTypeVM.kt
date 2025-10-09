package ua.gov.diia.ps_criminal_cert.ui.delivery_type

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
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertApplicationInfoNextStep
import ua.gov.diia.ps_criminal_cert.models.enums.RepeatedDeliveryNextStep
import ua.gov.diia.ps_criminal_cert.models.request.TypeDeliveryRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ADDRESS_SCHEMA_COURIER
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ADDRESS_SCHEMA_POST_OFFICE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DELIVERY_TYPE_ACTION_NEXT_STEP
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DELIVERY_TYPE_ACTION_SAVE_TYPE
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertDeliveryTypeVM @Inject constructor(
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
    private var rbSelected: Boolean = false
    private var selectedDeliveryTypeId: String = ""
    private var applicationId: String? = null

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.RADIO_BTN_GROUP_ORG -> {
                event.data?.let { data ->
                    rbSelected = true
                    selectedDeliveryTypeId = data
                    _bodyData.findAndChangeFirstByInstance<RadioBtnGroupOrgData> {
                        it.onItemClick(event.data)
                    }
                    _bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> {
                        it.activateButtonsIgnoreCheckbox(rbSelected)
                    }
                }
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_DELIVERY_TYPE_ACTION_SAVE_TYPE -> {
                saveDeliveryType(action.resource)
            }

            SCREEN_DELIVERY_TYPE_ACTION_NEXT_STEP -> {
                getRepeatedDeliveryNextStep()
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
            }
        }
    }

    private fun getRepeatedDeliveryNextStep(force: Boolean? = null) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.BTN_ACTION_NEXT_STEP_ID
            }
        ) {
            if (!applicationId.isNullOrEmpty()) {
                api.getCrimeCertRepeatedDeliveryNextStep(
                    applicationId = applicationId ?: return@executeActionOnFlow,
                    force = force,
                    addressId = null,
                    typeDelivery = selectedDeliveryTypeId
                ).let {
                    it.template?.let {
                        showTemplateDialog(it)
                    }
                    it.nextStep?.let {
                        when (it) {
                            RepeatedDeliveryNextStep.CONTACTS_REPEATED_DELIVERY.code -> {
                                _navigation.tryEmit(
                                    DeliveryTypeNavigation.ToContacts(
                                        applicationId,
                                        true,
                                        selectedDeliveryTypeId
                                    )
                                )
                            }

                            RepeatedDeliveryNextStep.POST_OFFICE_ADDRESS_SCHEME.code -> {
                                _navigation.tryEmit(
                                    DeliveryTypeNavigation.ToDeliveryAddress(
                                        applicationId,
                                        ADDRESS_SCHEMA_POST_OFFICE,
                                        true,
                                        selectedDeliveryTypeId
                                    )
                                )
                            }

                            RepeatedDeliveryNextStep.COURIER_ADDRESS_SCHEME.code -> {
                                _navigation.tryEmit(
                                    DeliveryTypeNavigation.ToDeliveryAddress(
                                        applicationId,
                                        ADDRESS_SCHEMA_COURIER,
                                        true,
                                        selectedDeliveryTypeId
                                    )
                                )
                            }

                            else -> {
                                //noting
                            }
                        }
                    }
                }
            }
        }
    }


    fun getScreenContent(id: String, repeatedDelivery: Boolean) {
        applicationId = id
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value =
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
            })
        {
            api.getCriminalCertDeliveryType(id, repeatedDelivery).let { response ->
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

    private fun saveDeliveryType(id: String?) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.SAVE_BUTTON_ID
            }) {
            val request = TypeDeliveryRequest(
                selectedDeliveryTypeId
            )
            id?.let {
                api.saveCriminalCertDeliveryType(it, request).let { response ->
                    if (response.template != null) {
                        showTemplateDialog(response.template)
                    }
                    when (response.nextStep) {
                        CriminalCertApplicationInfoNextStep.POST_OFFICE.code -> {
                            _navigation.tryEmit(
                                DeliveryTypeNavigation.ToDeliveryAddress(
                                    applicationId,
                                    ADDRESS_SCHEMA_POST_OFFICE,
                                    false,
                                    null
                                )
                            )
                        }

                        CriminalCertApplicationInfoNextStep.COURIER.code -> {
                            _navigation.tryEmit(
                                DeliveryTypeNavigation.ToDeliveryAddress(
                                    applicationId,
                                    ADDRESS_SCHEMA_COURIER,
                                    false,
                                    null
                                )
                            )
                        }

                        CriminalCertApplicationInfoNextStep.CONTACTS.code -> {
                            _navigation.tryEmit(
                                DeliveryTypeNavigation.ToContacts(
                                    applicationId,
                                    false,
                                    null
                                )
                            )
                        }
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

    sealed class DeliveryTypeNavigation : NavigationPath {
        data class ToDeliveryAddress(
            val id: String?,
            val schema: String,
            val repeatedDelivery: Boolean,
            val deliveryType: String?
        ) : DeliveryTypeNavigation()

        data class ToContacts(
            val id: String?,
            val repeatedDelivery: Boolean,
            val deliveryType: String?
        ) : DeliveryTypeNavigation()
    }
}