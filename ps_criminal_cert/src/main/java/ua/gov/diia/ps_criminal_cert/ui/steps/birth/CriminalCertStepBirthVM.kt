package ua.gov.diia.ps_criminal_cert.ui.steps.birth

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
import ua.gov.diia.ps_criminal_cert.models.request.BirthPlaceRequest
import ua.gov.diia.ps_criminal_cert.models.request.BirthPlaceSelectionRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_BIRTH_PLACE_ACTION_NEXT_STEP
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_BIRTH_PLACE_ACTION_SAVE_CHANGES
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_BIRTH_PLACE_DETAILS_STEP
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.container.BackgroundWhiteOrgData
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepBirthVM @Inject constructor(
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

    private val _navigation = MutableSharedFlow<NavigationPath>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    fun doInit(navBarTitle: String?) {
        this.navBarTitle = navBarTitle
    }

    var applicationId: String? = null
    private var navBarTitle: String? = null
    private var birthPlaceSelectionId: String? = null
    private var city: String? = null
    private var region: String? = null
    private var country: String = COUNTRY_DEFAULT

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.RADIO_BTN_GROUP_ORG -> {
                val checkBoxId = event.data
                _bodyData.findAndChangeFirstByInstance<RadioBtnGroupOrgData> {
                    birthPlaceSelectionId = checkBoxId
                    val org = it.onItemClick(checkBoxId)
                    changeButtonState(org.hasSelectedItem())
                    org
                }
            }

            UIActionKeysCompose.TEXT_INPUT -> {
                onInputChanged(id = event.optionalId, data = event.data)
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_BIRTH_PLACE_DETAILS_STEP, SCREEN_BIRTH_PLACE_ACTION_NEXT_STEP -> {
                doNextStepBirthPlaceRequest(
                    applicationId ?: return,
                    birthPlaceSelectionId ?: return
                )
            }

            SCREEN_BIRTH_PLACE_ACTION_SAVE_CHANGES -> {
                saveBirthPlace(applicationId ?: return)
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(force = false)
            }
        }
    }

    fun getScreenContent(id: String, isDetails: Boolean = false) {
        applicationId = id
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
        }) {
            api.getCriminalCertBirthPlace(id, isDetails).let { response ->
                response.template?.let { showTemplateDialog(it) }
                if (response.template == null) {
                    response.topGroup?.let {
                        setContextMenu(it.getEllipseMenu()?.toTypedArray())
                    }
                    showScreenContent(response)
                }
            }
        }
    }

    private fun saveBirthPlace(applicationId: String) {
        _bodyData.find { it is BackgroundWhiteOrgData }?.let {
            val bgWhiteOrg = (it as BackgroundWhiteOrgData)
            bgWhiteOrg.items.forEach { i ->
                if (i is TextInputMoleculeData) {
                    if (i.id == INPUT_TEXT_CITY_ID) {
                        city = i.inputValue
                    }
                    if (i.id == INPUT_TEXT_REGION_ID) {
                        region = i.inputValue
                    }
                }
                if (i is SelectorOrgData) {
                    country = i.inputValue ?: COUNTRY_DEFAULT
                }
            }
        }

        executeActionOnFlow(progressIndicator = _progressIndicator.also {
            _progressIndicatorKey.value = CriminalCertConst.SAVE_BUTTON_ID
        }) {
            val request = BirthPlaceRequest(city = city, country = country, region = region)
            api.saveCriminalCertBirthPlace(applicationId, request).let { response ->
                if (response.template != null) {
                    showTemplateDialog(response.template)
                }
                processNextStepNavigation(response.nextStep)
            }
        }
    }

    private fun processNextStepNavigation(nextStep: String?) {
        when (nextStep) {
            CriminalCertApplicationInfoNextStep.CITIZENSHIP.code -> {
                _navigation.tryEmit(BirthPlaceNavigation.ToCitizenship(applicationId))
            }

            CriminalCertApplicationInfoNextStep.FORMAT_EXTRACT.code -> {
                _navigation.tryEmit(BirthPlaceNavigation.ToFormatExtract(applicationId))
            }

            CriminalCertApplicationInfoNextStep.CONTACTS.code -> {
                _navigation.tryEmit(BirthPlaceNavigation.ToContacts(applicationId))
            }

            CriminalCertApplicationInfoNextStep.BIRTH_PLACE.code -> {
                clearScreenContent()
                getScreenContent(applicationId ?: return, isDetails = true)
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

    private fun onInputChanged(id: String?, data: String?) {
        if (data == null) return
        _bodyData.findAndChangeFirstByInstance<BackgroundWhiteOrgData> {
            it.onInputChanged(id = id, newValue = data)
        }
        when (id) {
            INPUT_TEXT_CITY_ID -> {
                city = data
            }

            INPUT_TEXT_REGION_ID -> {
                region = data
            }
        }
        changeButtonStateByInput()
    }

    private fun changeButtonState(isInputValid: Boolean) {
        val currentButton = _bottomData.first { it is BottomGroupOrgData } as BottomGroupOrgData

        _bottomData[0] = currentButton.changeStateByValidation(
            if (isInputValid) UIState.Interaction.Enabled else UIState.Interaction.Disabled
        )
    }

    private fun changeButtonStateByInput() {
        val index = _bottomData.indexOfFirst {
            it is BottomGroupOrgData
        }
        if (index != -1) {
            val oldValue: BottomGroupOrgData = _bottomData[index] as BottomGroupOrgData
            val newValue = oldValue.copy(
                primaryButton = oldValue.primaryButton?.copy(
                    interactionState = if (isAllFormsValid()) {
                        UIState.Interaction.Enabled
                    } else {
                        UIState.Interaction.Disabled
                    }
                )
            )
            _bottomData[index] = newValue
        }
    }

    private fun isAllFormsValid(): Boolean {
        val forms = _bodyData.filterIsInstance<BackgroundWhiteOrgData>()
        return forms.all { it.isFormFilledAndValid() }
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
                    it.template?.let { t -> showTemplateDialog(t) }
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

    private fun clearScreenContent() {
        _bodyData.clear()
        _topGroupData.clear()
        _bottomData.clear()
    }

    private fun doNextStepBirthPlaceRequest(applicationId: String, birthPlaceSelectionId: String) {
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
        }) {
            val nextStepResponse = api.getNextStepBirthPlace(
                applicationId,
                BirthPlaceSelectionRequest(birthPlaceSelection = birthPlaceSelectionId)
            )
            nextStepResponse.template?.let { t -> showTemplateDialog(t) }
            processNextStepNavigation(nextStepResponse.nextStep)
        }
    }

    sealed class BirthPlaceNavigation : NavigationPath {
        data class ToFormatExtract(val id: String?) : BirthPlaceNavigation()
        data class ToCitizenship(val id: String?) : BirthPlaceNavigation()
        data class ToContacts(val applicationId: String? = null) : BirthPlaceNavigation()

    }
}

private const val INPUT_TEXT_CITY_ID = "city"
private const val INPUT_TEXT_REGION_ID = "region"
private const val COUNTRY_DEFAULT = "Україна"
