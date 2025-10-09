package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

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
import ua.gov.diia.address_search.models.NationalityItem
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
import ua.gov.diia.ps_criminal_cert.models.request.Citizenship
import ua.gov.diia.ps_criminal_cert.models.request.NationalitiesRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.BTN_CANCEL_APPLICATION_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_NATIONALITIES_ACTION_ADD_BLOCK
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_NATIONALITIES_ACTION_DELETE_BLOCK
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_NATIONALITIES_ACTION_SAVE_CITIZENSHIP
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_NATIONALITIES_SELECTOR_FIRST_ID
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
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
class CriminalCertStepNationalityVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val withContextMenu: WithContextMenu<ContextMenuField>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val navigationHelper: PSNavigationHelper,
    private val withRatingDialog: WithRatingDialogOnFlow,
    private val composeMapper: CriminalCertNationalitiesMapper
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    PSNavigationHelper by navigationHelper,
    WithContextMenu<ContextMenuField> by withContextMenu,
    WithRatingDialogOnFlow by withRatingDialog,
    CriminalCertNationalitiesMapper by composeMapper {

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
    private var nationalitySelected: Boolean = false
    private var nationalities = listOf<NationalityItem>()
    private val selectedNationalityItemsList = mutableStateListOf<NationalityItem>()
    private var selectedNationalityItem: NationalityItem? = null
    private var selectorId: String? = ""

    private val selectedNationalityIds: Set<String>
        get() = buildSet {
            addAll(selectedNationalityItemsList.mapNotNull { it.id })
            selectedNationalityItem?.id?.let { add(it) }
        }

    private val availableNationalities: List<NationalityItem>
        get() = nationalities.filter { it.id !in selectedNationalityIds }

    fun onUIAction(event: UIAction) {

        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.SELECTOR_ORG -> {
                selectorId = event.data
                nationalitySelected = event.data != SCREEN_NATIONALITIES_SELECTOR_FIRST_ID
                _navigation.tryEmit(
                    NationalityNavigation.NavigateToNationalitySelection(
                        contextMenuArray = withContextMenu.getMenu(),
                        navBarTitle = navBarTitle,
                        nationalities = availableNationalities
                    )
                )
            }
        }

        val action = event.action
        when (action?.type) {
            SCREEN_NATIONALITIES_ACTION_SAVE_CITIZENSHIP -> {
                saveNationalities(action.resource)
            }

            SCREEN_NATIONALITIES_ACTION_DELETE_BLOCK -> {
                _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
                    it.removeOrCollapseItem(event.data)
                }
                val itemToRemove = selectedNationalityItemsList.firstOrNull {
                    it.containerId == event.data?.let { id ->
                        getContainerId(id)
                    }
                }
                if (itemToRemove != null) {
                    selectedNationalityItemsList.remove(itemToRemove)
                }
                changeBottomGroupOrgState()
                changeRecursiveContainerBtnState()
            }

            SCREEN_NATIONALITIES_ACTION_ADD_BLOCK -> {
                _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
                    it.expandOrAddItem()
                }
                changeBottomGroupOrgState()
                changeRecursiveContainerBtnState()
            }

            BTN_CANCEL_APPLICATION_ACTION -> {
                cancelApplication(false)
            }
        }
    }

    fun getScreenContent(id: String, countryCode: String?) {
        if (nationalitySelected) return
        applicationId = id
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value =
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
            })
        {
            api.getCriminalCertNationalities(id, countryCode).let { response ->
                response.template?.let { showTemplateDialog(it) }
                if (response.template == null) {
                    response.topGroup?.let { setContextMenu(it.getEllipseMenu()?.toTypedArray()) }
                    showScreenContent(response)
                }
            }
        }
    }

    private fun getContainerId(data: String): String {
        val idList: List<String> = data.split("\\s")
        return idList[0]
    }

    private fun saveNationalities(id: String?) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value = CriminalCertConst.SAVE_NATIONALITIES_BUTTON_ID
            }) {
            val nationalities: List<Citizenship> =
                selectedNationalityItemsList.map { Citizenship(citizenshipCountry = it.id) }
            val request = NationalitiesRequest(
                nationalities, selectedNationalityItem?.id
            )
            id?.let {
                api.saveCriminalCertNationalities(it, request).let { response ->
                    if (response.template != null) {
                        showTemplateDialog(response.template)
                    }
                    when (response.nextStep) {
                        CriminalCertApplicationInfoNextStep.FORMAT_EXTRACT.code -> {
                            _navigation.tryEmit(
                                NationalityNavigation.NavigateToFormatExtract(
                                    applicationId
                                )
                            )
                        }
                        CriminalCertApplicationInfoNextStep.CONTACTS.code -> {
                            _navigation.tryEmit(
                                NationalityNavigation.NavigateToContacts(
                                    applicationId
                                )
                            )
                        }
                    }
                    if (response.template?.data?.mainButton?.resource != null) {
                        applicationId = response.template.data.mainButton.resource
                    }
                }
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
            item.backgroundWhiteOrg?.items?.forEach { backgroundWhiteOrgItem ->
                backgroundWhiteOrgItem.selectorOrg?.let { selectorOrg ->
                    selectorOrg.selectorListWidgetOrg?.let {
                        nationalities = toNationalityList(it.items)
                    }
                }
            }
        }
        content.bottomGroup?.forEach { item ->
            item.mapToComposeBottomData()?.let {
                _bottomData.addAllIfNotNull(it)
            }
        }
    }

    fun clearContent() {
        if (!nationalitySelected) {
            _topGroupData.clear()
            _bodyData.clear()
            _bottomData.clear()
        }
    }

    fun selectNationalityItem(positionId: String?, positionName: String) {
        if (selectorId == SCREEN_NATIONALITIES_SELECTOR_FIRST_ID) {
            selectNationalityItemBackgroundWhite(positionId, positionName)
        } else {
            selectNationalityItemRecursive(positionId, positionName)
        }
    }

    private fun selectNationalityItemRecursive(positionId: String?, positionName: String) {
        val containerId = selectorId?.let { getContainerId(it) }
        val newItem =
            nationalities.firstOrNull { it.id == positionId }?.copy(containerId = containerId)

        if (newItem != null) {
            val previouslySelected = selectedNationalityItemsList
                .firstOrNull { it.containerId == containerId }

            previouslySelected?.let { selectedNationalityItemsList.remove(it) }

            selectedNationalityItemsList.add(newItem)
        }

        _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> {
            it.onInputChanged(selectorId, positionName)
        }
        changeRecursiveContainerBtnState()
        changeBottomGroupOrgState()
    }


    private fun selectNationalityItemBackgroundWhite(positionId: String?, positionName: String) {
        selectedNationalityItem = nationalities.firstOrNull { it.id == positionId }

        _bodyData.findAndChangeFirstByInstance<BackgroundWhiteOrgData> {
            it.onInputChanged(selectorId, positionName)
        }
        changeRecursiveContainerBtnState()
        changeBottomGroupOrgState()
    }


    private fun changeRecursiveContainerBtnState() {
        _bodyData.findAndChangeFirstByInstance<RecursiveContainerOrgData> { item ->
            val isFull = item.items.size >= (item.itemsMaxSize
                ?: 0)
            item.changeBtnState(
                if (selectedNationalityItem?.id != "999" && !isFull && isFormFilledAndValid())

                    UIState.Interaction.Enabled else
                    UIState.Interaction.Disabled
            )
        }
    }

    private fun isFormFilledAndValid(): Boolean {
        val forms = _bodyData.filterIsInstance<RecursiveContainerOrgData>()
        return forms.all { it.isFormFilledAndValid() }
    }

    private fun changeBottomGroupOrgState() {
        _bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> { item ->
            item.changeStateByValidation(
                if (isFormFilledAndValid()) UIState.Interaction.Enabled else UIState.Interaction.Disabled
            )
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

    sealed class NationalityNavigation : NavigationPath {
        data class NavigateToNationalitySelection(
            val contextMenuArray: Array<ContextMenuField>? = null,
            val navBarTitle: String? = null,
            val nationalities: List<NationalityItem>
        ) : NationalityNavigation()

        data class NavigateToFormatExtract(val applicationId: String? = null) :
            NationalityNavigation()

        data class NavigateToContacts(val applicationId: String? = null) : NationalityNavigation()
    }
}