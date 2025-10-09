package ua.gov.diia.ps_criminal_cert.ui.steps.requester

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
import ua.gov.diia.ps_criminal_cert.models.request.FullNameBefore
import ua.gov.diia.ps_criminal_cert.models.request.FullNamesBeforeRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_ACTION_ADD_BLOCK
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_ACTION_DELETE_BLOCK
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_ACTION_SAVE_CHANGES
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_INPUT_FIRST_NAME_ID
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_INPUT_LAST_NAME_ID
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_REQUESTER_INPUT_MIDDLE_NAME_ID
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.container.BackgroundWhiteOrgData
import ua.gov.diia.ui_base.components.organism.input.RecursiveContainerOrgData
import ua.gov.diia.ui_base.mappers.getEllipseMenu
import ua.gov.diia.ui_base.mappers.mapToComposeBodyData
import ua.gov.diia.ui_base.mappers.mapToComposeBottomData
import ua.gov.diia.ui_base.mappers.mapToComposeTopData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepRequesterVM @Inject constructor(
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

    fun doInit(navBarTitle: String?) {
        this.navBarTitle = navBarTitle
    }

    private var navBarTitle: String? = null
    var applicationId: String? = null

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.TEXT_INPUT -> {
                _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
                    it.onInputChanged(event.data)
                }
                changeBottomGroupOrgState()
                changeRecursiveContainerBtnState()
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_REQUESTER_ACTION_DELETE_BLOCK -> {
                _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
                    it.removeOrCollapseItem(event.data)
                }
                changeBottomGroupOrgState()
                changeRecursiveContainerBtnState()
            }

            SCREEN_REQUESTER_ACTION_ADD_BLOCK -> {
                _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
                    it.expandOrAddItem()
                }
                changeBottomGroupOrgState()
                changeRecursiveContainerBtnState()
            }

            SCREEN_REQUESTER_ACTION_SAVE_CHANGES -> {
                val dataList = _bodyData
                    .filterIsInstance<RecursiveContainerOrgData>().flatMap {
                        it.items.toList()
                    }
                val answers = getInputItemsValue(dataList)
                saveRequesterNames(answers)
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
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
            api.getCriminalCertRequester(id).let { response ->
                response.topGroup?.let { setContextMenu(it.getEllipseMenu()?.toTypedArray()) }
                showScreenContent(response)
                response.template?.let { showTemplateDialog(it) }
            }
        }
    }

    fun cancelApplication(force: Boolean) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.BTN_CANCEL_ID
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

    private fun changeBottomGroupOrgState() {
        _bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> { item ->
            item.changeStateByValidation(
                if (isFormFilledAndValid()) UIState.Interaction.Enabled else UIState.Interaction.Disabled
            )
        }
    }

    private fun changeRecursiveContainerBtnState() {
        _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> { item ->
            val isFull = item.items.size >= (item.itemsMaxSize
                ?: 0)
            item.changeBtnState(
                if (isFormFilledAndValid() && !isFull)

                    UIState.Interaction.Enabled else
                    UIState.Interaction.Disabled
            )
        }
    }

    private fun getInputItemsValue(
        backgroundWhiteOrgDataList: List<BackgroundWhiteOrgData>,
    ): MutableList<FullNameBefore> {
        val answers = mutableListOf<FullNameBefore>()
        for (backgroundWhiteOrgData in backgroundWhiteOrgDataList) {
            var firstNameBefore: String? = null
            var middleNameBefore: String? = null
            var lastNameBefore: String? = null
            backgroundWhiteOrgData.items.forEach { item ->
                if (item is TextInputMoleculeData) {
                    when (item.inputCode) {
                        SCREEN_REQUESTER_INPUT_FIRST_NAME_ID -> firstNameBefore = item.inputValue
                        SCREEN_REQUESTER_INPUT_MIDDLE_NAME_ID -> middleNameBefore =
                            if (!item.inputValue.isNullOrBlank()) item.inputValue else null

                        SCREEN_REQUESTER_INPUT_LAST_NAME_ID -> lastNameBefore = item.inputValue
                    }
                }
            }
            if (firstNameBefore != null && lastNameBefore != null) {
                answers.addTextAnswer(
                    firstNameBefore = firstNameBefore,
                    middleNameBefore = middleNameBefore,
                    lastNameBefore = lastNameBefore
                )
            }
        }
        return answers
    }

    private fun MutableList<FullNameBefore>.addTextAnswer(
        firstNameBefore: String?,
        middleNameBefore: String? = null,
        lastNameBefore: String?
    ) {
        if (firstNameBefore != null && lastNameBefore != null) {
            this.add(
                FullNameBefore(
                    firstNameBefore = firstNameBefore,
                    middleNameBefore = middleNameBefore,
                    lastNameBefore = lastNameBefore
                )
            )
        }
    }

    private fun isFormFilledAndValid(): Boolean {
        val forms = _bodyData.filterIsInstance<RecursiveContainerOrgData>()
        return forms.all { it.isFormFilledAndValid() }
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

    private fun saveRequesterNames(fullNames: List<FullNameBefore>) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.SAVE_NAME_BUTTON_ID
            }) {
            val request = FullNamesBeforeRequest(
                fullNamesBefore = fullNames
            )
            applicationId?.let {
                api.saveCriminalCertRequester(it, request).let { response ->
                    if (response.template != null) {
                        showTemplateDialog(response.template)
                    }
                    if (response.template?.data?.mainButton?.resource != null) {
                        applicationId = response.template.data.mainButton.resource
                    }
                    if (response.nextStep == CriminalCertApplicationInfoNextStep.BIRTH_PLACE.code) {
                        _navigation.tryEmit(RequesterNavigation.NavigateToBirthPlace(applicationId))
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

    sealed class RequesterNavigation : NavigationPath {
        data class NavigateToBirthPlace(val id: String?) : RequesterNavigation()
    }
}