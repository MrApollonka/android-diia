package ua.gov.diia.ps_criminal_cert.ui.details

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
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
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.PAYMENT_SDK_ERROR
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.delegation.download_files.base64.DownloadableBase64File
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.payment.delegation.WithShareReceiptOnFlow
import ua.gov.diia.payment.helper.PaymentLaunchHelper
import ua.gov.diia.payment.models.PaymentStatusCode
import ua.gov.diia.payment.models.ReceiptAction
import ua.gov.diia.payment.repository.PaymentRepository
import ua.gov.diia.ps_criminal_cert.models.enums.RepeatedDeliveryNextStep
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.payment.CriminalCertFlow
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ADDRESS_SCHEMA_COURIER
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ADDRESS_SCHEMA_POST_OFFICE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.PAYMENT_BUTTON_ID
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.RATING_SERVICE_CATEGORY
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.RATING_SERVICE_CODE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_ADDRESS_COURIER
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_ADDRESS_POST_OFFICE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_BIRTH_PLACE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_CANCEL_APPLICATION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_CHANGE_NAME
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_CITIZENSHIP
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_CONFIRMATION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_CONTACTS
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_DELIVERY_TYPE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_DOWNLOAD
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_FORMAT_EXTRACT
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_PAYMENT
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_PREVIEW
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_RECEIPT
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_ACTION_REPEATED_DELIVERY
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_DETAILS_CANCEL_RD_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_HOME_ACTION_NEW_APPLICATION
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertStatusVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val paymentRepository: PaymentRepository,
    private val withContextMenu: WithContextMenu<ContextMenuField>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val navigationHelper: PSNavigationHelper,
    private val withRatingDialog: WithRatingDialogOnFlow,
    private val shareReceipt: WithShareReceiptOnFlow,
    private val paymentLaunchHelper: PaymentLaunchHelper
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    PSNavigationHelper by navigationHelper,
    WithContextMenu<ContextMenuField> by withContextMenu,
    WithRatingDialogOnFlow by withRatingDialog,
    WithShareReceiptOnFlow by shareReceipt,
    PaymentLaunchHelper by paymentLaunchHelper {

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

    private var applicationId: String? = null
    private var resourceId: String? = null

    private val _paymentDataJson = MutableLiveData<UiDataEvent<String>>()
    val paymentDataJson = _paymentDataJson.asLiveData()

    private val _shareCert = mutableSharedFlowOf<DownloadableBase64File>()
    val shareCert = _shareCert.asSharedFlow()

    private val _downloadCert = mutableSharedFlowOf<DownloadableBase64File>()
    val downloadCert = _downloadCert.asSharedFlow()

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }
        }
        val action = event.action
        when (action?.type) {

            SCREEN_DETAILS_ACTION_DOWNLOAD -> {
                _navigation.tryEmit(StatusNavigation.ShareLink(action.resource))
            }

            SCREEN_DETAILS_ACTION_PREVIEW -> {
                _navigation.tryEmit(StatusNavigation.ToOpenLink(action.resource))
            }

            SCREEN_DETAILS_ACTION_RECEIPT -> {
                action.resource?.let { loadReceipt(it, event.data) }
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
            }

            SCREEN_DETAILS_CANCEL_RD_ACTION -> {
                cancelRepeatedDeliveryApplication(false)
            }

            SCREEN_DETAILS_ACTION_CHANGE_NAME -> {
                _navigation.tryEmit(StatusNavigation.ToRequester(action.resource))
            }

            SCREEN_DETAILS_ACTION_CITIZENSHIP -> {
                _navigation.tryEmit(StatusNavigation.ToNationalities(action.resource))
            }

            SCREEN_DETAILS_ACTION_FORMAT_EXTRACT -> {
                _navigation.tryEmit(StatusNavigation.ToFormatExtract(action.resource))
            }

            SCREEN_DETAILS_ACTION_BIRTH_PLACE -> {
                _navigation.tryEmit(StatusNavigation.ToBirthPlace(action.resource))
            }

            SCREEN_DETAILS_ACTION_DELIVERY_TYPE -> {
                _navigation.tryEmit(StatusNavigation.ToDeliveryType(action.resource, false))
            }

            SCREEN_DETAILS_ACTION_CONTACTS -> {
                _navigation.tryEmit(StatusNavigation.ToContacts(action.resource, false))
            }

            SCREEN_DETAILS_ACTION_ADDRESS_COURIER -> {
                _navigation.tryEmit(
                    StatusNavigation.ToDeliveryAddress(
                        action.resource,
                        ADDRESS_SCHEMA_COURIER,
                        false
                    )
                )
            }

            SCREEN_DETAILS_ACTION_ADDRESS_POST_OFFICE -> {
                _navigation.tryEmit(
                    StatusNavigation.ToDeliveryAddress(
                        action.resource,
                        ADDRESS_SCHEMA_POST_OFFICE,
                        false
                    )
                )
            }

            SCREEN_DETAILS_ACTION_CONFIRMATION -> {
                _navigation.tryEmit(
                    StatusNavigation.ToConfirmation(action.resource)
                )
            }

            SCREEN_HOME_ACTION_NEW_APPLICATION -> {
                _navigation.tryEmit(StatusNavigation.ToHomeScreen(newApplication = true))
            }

            SCREEN_DETAILS_ACTION_PAYMENT -> {
                val subresource = action.subresource
                val resource = action.resource
                loadPaymentData(resource, subresource)
            }

            SCREEN_DETAILS_ACTION_REPEATED_DELIVERY -> {
                applicationId = action.resource
                getRepeatedDeliveryNextStep()
            }

            SCREEN_DETAILS_ACTION_CANCEL_APPLICATION -> {
                applicationId = action.resource
                cancelApplication(false)
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

    private fun getRepeatedDeliveryNextStep(force: Boolean? = null) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.CREATE_BUTTON_ID
            }
        ) {
            if (!applicationId.isNullOrEmpty()) {
                api.getCrimeCertRepeatedDeliveryNextStep(
                    applicationId = applicationId ?: return@executeActionOnFlow,
                    force = force,
                    addressId = null,
                    typeDelivery = null
                ).let {
                    it.template?.let {
                        showTemplateDialog(it)
                    }
                    it.nextStep?.let {
                        when (it) {
                            RepeatedDeliveryNextStep.REPEATED_DELIVERY_TYPE.code -> {
                                _navigation.tryEmit(
                                    StatusNavigation.ToDeliveryType(
                                        applicationId,
                                        true
                                    )
                                )
                            }

                            RepeatedDeliveryNextStep.CONTACTS_REPEATED_DELIVERY.code -> {
                                _navigation.tryEmit(
                                    StatusNavigation.ToContacts(
                                        applicationId,
                                        true
                                    )
                                )
                            }

                            RepeatedDeliveryNextStep.POST_OFFICE_ADDRESS_SCHEME.code -> {
                                _navigation.tryEmit(
                                    StatusNavigation.ToDeliveryAddress(
                                        applicationId,
                                        ADDRESS_SCHEMA_POST_OFFICE,
                                        true
                                    )
                                )
                            }

                            RepeatedDeliveryNextStep.COURIER_ADDRESS_SCHEME.code -> {
                                _navigation.tryEmit(
                                    StatusNavigation.ToDeliveryAddress(
                                        applicationId,
                                        ADDRESS_SCHEMA_COURIER,
                                        true
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

    fun sendResultCode(respCode: String?) {
        val resource = resourceId ?: return
        executeActionOnFlow {
            val resp = paymentRepository.setPaymentStatusCode(
                paymentFlow = CriminalCertFlow,
                resId = resource,
                paymentStatusCode = PaymentStatusCode(statusCode = if (respCode.isNullOrEmpty()) PAYMENT_SDK_ERROR else respCode)
            )
            resp.template?.let { showTemplateDialog(it) }
            if (resp.template?.data?.mainButton?.resource != null) {
                applicationId = resp.template?.data?.mainButton?.resource
            }
        }
    }

    fun getScreenContent(id: String) {
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL
            })
        {
            applicationId = id
            api.getCriminalCertsDetails(id).let { response ->
                response.template?.let { showTemplateDialog(it) }
                if (response.template == null) {
                    response.topGroup?.let { setContextMenu(it.getEllipseMenu()?.toTypedArray()) }
                    showScreenContent(response)
                }
                response.ratingForm?.let { ratingForm ->
                    showRatingDialog(
                        ratingForm.copy(
                            showInAppReview = true
                        )
                    )
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

    private fun loadReceipt(
        resourceId: String,
        uiItemId: String?
    ) {
        prepareReceipt(
            paymentFlow = CriminalCertFlow,
            id = resourceId,
            receiptAction = ReceiptAction.OPEN
        ).also {
            _progressIndicatorKey.value = uiItemId.orEmpty()
        }
    }

    fun refreshData() {
        _topGroupData.clear()
        _bottomData.clear()
        _bodyData.clear()
        applicationId?.let { getScreenContent(it) }
    }

    private fun loadPaymentData(res: String?, subRes: String?) {
        resourceId = res
        val resId = res ?: return
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = PAYMENT_BUTTON_ID
            }
        ) {
            val paymentData = paymentRepository.getPaymentDataJson(
                CriminalCertFlow,
                resId,
                "bank_id"
            )
            paymentData.template?.let { showTemplateDialog(it) }
            paymentData.dataJson?.let { _paymentDataJson.value = UiDataEvent(it) }
        }
    }

    fun cancelRepeatedDeliveryApplication(force: Boolean) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.CANCEL_BUTTON_ID
            }
        ) {
            if (!applicationId.isNullOrEmpty()) {
                api.cancelCriminalCertRepeatedDelivery(
                    id = applicationId ?: return@executeActionOnFlow,
                    force = force,
                ).let {
                    it.template?.let {
                        showTemplateDialog(it)
                    }
                }
            }
        }
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(ratingRequest, RATING_SERVICE_CATEGORY, RATING_SERVICE_CODE)
    }

    fun getRatingForm() {
        getRating(RATING_SERVICE_CATEGORY, RATING_SERVICE_CODE)
    }

    sealed class StatusNavigation : NavigationPath {
        data class ToRequester(val id: String?) : StatusNavigation()
        data class ToNationalities(val id: String?) : StatusNavigation()
        data class ToFormatExtract(val id: String?) : StatusNavigation()
        data class ToBirthPlace(val id: String?) : StatusNavigation()

        data class ToDeliveryType(
            val id: String?,
            val repeatedDelivery: Boolean
        ) : StatusNavigation()

        data class ToContacts(
            val id: String?,
            val repeatedDelivery: Boolean
        ) : StatusNavigation()

        data class ToDeliveryAddress(
            val id: String?,
            val scheme: String,
            val repeatedDelivery: Boolean
        ) : StatusNavigation()

        data class ToHomeScreen(val newApplication: Boolean) : StatusNavigation()
        data class ToConfirmation(val id: String?) : StatusNavigation()
        data class ShareLink(val link: String?) : StatusNavigation()
        data class ToOpenLink(val url: String?) : StatusNavigation()
    }

}