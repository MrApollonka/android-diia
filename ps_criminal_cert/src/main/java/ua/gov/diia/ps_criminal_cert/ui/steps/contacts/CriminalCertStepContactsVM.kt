package ua.gov.diia.ps_criminal_cert.ui.steps.contacts

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
import ua.gov.diia.ps_criminal_cert.models.request.ContactsRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_CONTACTS_ACTION_CONFIRMATION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_CONTACTS_ACTION_SAVE_CONTACTS
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_CONTACTS_EMAIL
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgData
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepContactsVM @Inject constructor(
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

    private var idEmail: String? = null
    private var userEmail: String? = null
    private var regexpEmail: String? = null

    private var navBarTitle: String? = null
    private var phoneNumber: String = ""
    private var codeValueId: String? = null
    private var codelabel: String? = null
    private var applicationId: String? = null

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.TEXT_INPUT -> {
                onNewValue(event.optionalId, event.data)
            }

            UIActionKeysCompose.INPUT_PHONE_MLC -> {
                onNewValue(event.optionalId, event.action?.resource)
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_CONTACTS_ACTION_SAVE_CONTACTS -> {
                saveContacts(action.resource)
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
            }

            UIActionKeysCompose.INPUT_PHONE_MLC -> {
                onNewValue(event.action?.type, event.action?.resource)
            }

            SCREEN_CONTACTS_ACTION_CONFIRMATION -> {
                _navigation.tryEmit(
                    ContactsNavigation.ToRepeatedDeliveryConfirmation(
                        id = action.resource,
                        phone = phoneNumber,
                        email = userEmail
                    )
                )
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
            api.getCriminalCertContacts(id, repeatedDelivery).let { response ->
                response.template?.let { showTemplateDialog(it) }
                if (response.template == null) {
                    response.topGroup?.let { setContextMenu(it.getEllipseMenu()?.toTypedArray()) }
                    showScreenContent(response)
                }
            }
        }
    }

    private fun onNewValue(id: String?, data: String?) {
        if (data == null) return
        val index = _bodyData.indexOfFirst { it is QuestionFormsOrgData }

        _bodyData.findAndChangeFirstByInstance<QuestionFormsOrgData> {
            it.onInputChanged(id = id, newValue = data).also {
                if (id == SCREEN_CONTACTS_EMAIL) {
                    userEmail = data
                }
                if (id == "inputPhoneMlc") {
                    phoneNumber = data
                }
            }
        }

        _bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> { item ->
            val isValid = (_bodyData[index] as QuestionFormsOrgData).isFormFilledAndValid()
            item.changeStateByValidation(
                if (isValid && (phoneNumber.isNotEmpty())) UIState.Interaction.Enabled else UIState.Interaction.Disabled
            )
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
            item.questionFormsOrg?.let { qfOrg ->
                qfOrg.items.forEach { fItem ->
                    fItem.inputTextMlc?.let { inputText ->
                        inputText.id?.let { id ->
                            when (id) {
                                "email" -> {
                                    if (idEmail == null) idEmail = id
                                    userEmail = inputText.value
                                    regexpEmail = inputText.validation?.first()?.regexp
                                    onNewValue(inputText.id, inputText.value)
                                }
                            }
                        }
                    }
                    fItem.inputPhoneCodeOrg?.let {
                        codeValueId = fItem.inputPhoneCodeOrg?.codeValueId
                        codelabel =
                            fItem.inputPhoneCodeOrg?.codes?.find { it.id == codeValueId }?.value
                    }
                }
            }

        }
        content.bottomGroup?.forEach { item ->
            item.mapToComposeBottomData()?.let {
                _bottomData.addAllIfNotNull(it)
            }
        }
        val index = _bodyData.indexOfFirst { it is QuestionFormsOrgData }
        _bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> { item ->
            val isValid = (_bodyData[index] as QuestionFormsOrgData).isFormFilledAndValid()
            item.changeStateByValidation(
                if (isValid && (phoneNumber.isNotEmpty())) UIState.Interaction.Enabled else UIState.Interaction.Disabled
            )
        }
    }

    private fun saveContacts(id: String?) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.SCREEN_CONTACTS_SAVE_BTN_ID
            }) {
            val request = ContactsRequest(
                phoneNumber = codelabel + phoneNumber,
                emailAddress = userEmail
            )
            id?.let {
                api.saveCriminalCertContacts(it, request).let { response ->
                    if (response.template != null) {
                        showTemplateDialog(response.template)
                    }
                    when (response.nextStep) {
                        CriminalCertApplicationInfoNextStep.CONFIRMATION.code -> {
                            _navigation.tryEmit(ContactsNavigation.ToConfirmation(applicationId))
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

    sealed class ContactsNavigation : NavigationPath {
        data class ToConfirmation(val id: String?) : ContactsNavigation()
        data class ToRepeatedDeliveryConfirmation(
            val id: String?,
            val phone: String?,
            val email: String?
        ) : ContactsNavigation()
    }
}