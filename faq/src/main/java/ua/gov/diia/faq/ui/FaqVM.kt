package ua.gov.diia.faq.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.TridentWithBackNav
import ua.gov.diia.core.util.state.setFullScreen
import ua.gov.diia.core.util.state.stop
import ua.gov.diia.faq.helper.FaqHelper
import ua.gov.diia.faq.model.CategoryInfo
import ua.gov.diia.faq.model.Faq
import ua.gov.diia.faq.model.FaqCategoryCode
import ua.gov.diia.faq.model.FaqItem
import ua.gov.diia.faq.repository.FaqRepository
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.input.Mode
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.table.TableBlockAccordionOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class FaqVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val faqRepository: FaqRepository,
    private val withErrorHandlingOnFlow: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
    private val faqHelper: FaqHelper
) : ViewModel(),
    WithRetryLastAction by withRetryLastAction,
    WithErrorHandlingOnFlow by withErrorHandlingOnFlow {

    private val categoryId = savedStateHandle.get<String>("categoryId").orEmpty()
    private val isRoot = savedStateHandle.get<Boolean>("isRoot") ?: false

    private val _loader = MutableStateFlow(Loader.create())
    val loader: Flow<Loader> = _loader

    private val _top = mutableStateListOf<UIElementData>()
    val top: SnapshotStateList<UIElementData> = _top

    private val _body = mutableStateListOf<UIElementData>()
    val body: SnapshotStateList<UIElementData> = _body

    private val _navigationEvent = mutableSharedFlowOf<NavigationPath>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        fetchData()
        observeDataChange()
    }

    fun onUIAction(action: UIAction) {
        when (action.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigationEvent.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                _navigationEvent.tryEmit(Navigation.NavigateToSearch)
            }

            UIActionKeysCompose.LIST_ITEM_MLC -> {
                _navigationEvent.tryEmit(
                    Navigation.NavigateToSelf(
                        categoryId = action.action?.type ?: return
                    )
                )
            }
        }
    }

    private fun fetchData() {
        if (isRoot) {
            executeActionOnFlow(
                loader = _loader.setFullScreen(TridentWithBackNav)
                    .takeIf { faqRepository.data.value == null },
                dispatcher = Dispatchers.IO
            ) {
                faqRepository.load()
            }
        }
    }

    private fun observeDataChange() {
        viewModelScope.launch {
            faqRepository.data
                .filterNotNull()
                .collectLatest { faq ->
                    _top.clear()
                    _body.clear()

                    if (isRoot && categoryId.isEmpty()) {
                        createRootScreen(faq)
                    } else {
                        val categoryInfoList = faqRepository.getCategoriesInfo(
                            faq = faq,
                            code = faqHelper.getFaqCategoryCodeByDocumentCode(categoryId)
                        )

                        // If categories is empty just show FAQ root screen
                        if (categoryInfoList.isEmpty()) {
                            createRootScreen(faq)
                            return@collectLatest
                        }

                        _top.addAllIfNotNull(
                            NavigationPanelMlcData(
                                title = categoryInfoList.firstOrNull()?.parentName.toDynamicString(),
                                isContextMenuExist = false
                            )
                        )

                        categoryInfoList
                            .filter { it.isCategoryGroup }
                            .map { categoryInfo ->
                                createListItemMlcData(categoryInfo)
                            }.apply {
                                _body.addAllIfNotNull(
                                    ListItemGroupOrgData(
                                        itemsList = this
                                    )
                                )
                            }

                        categoryInfoList
                            .filter { !it.isCategoryGroup }
                            .mapNotNull { categoryInfo ->
                                faqRepository.getCategoryItem(
                                    faq = faq,
                                    code = categoryInfo.code
                                )?.let { categoryItem ->
                                    categoryItem.faq.map { faqItem ->
                                        createTableBlockAccordionOrgData(faqItem)
                                    }
                                }
                            }
                            .flatten()
                            .apply {
                                _body.addAllIfNotNull(
                                    *this.toTypedArray()
                                )
                            }
                    }

                    _loader.stop()
                }
        }
    }

    private fun createRootScreen(faq: Faq) {
        val rootCategoryTitle = faq.categoriesGroups
            .firstOrNull { categoryGroup -> categoryGroup.code == FaqCategoryCode.ROOT_GROUP_NAME.code }
            ?.title
            ?: "Питання та відповіді"
        _top.addAllIfNotNull(
            NavigationPanelMlcData(
                title = UiText.DynamicString(rootCategoryTitle),
                isContextMenuExist = false
            ),
            SearchInputV2Data(
                placeholder = UiText.DynamicString("Що шукаєте?"),
                mode = Mode.BUTTON.value
            )
        )

        faqRepository.getCategoriesInfo(
            faq = faq,
            code = FaqCategoryCode.ROOT_GROUP_NAME.code
        ).map { categoryInfo ->
            createListItemMlcData(categoryInfo)
        }.apply {
            _body.addAllIfNotNull(
                ListItemGroupOrgData(
                    itemsList = this
                )
            )
        }
    }

    private fun createListItemMlcData(categoryInfo: CategoryInfo): ListItemMlcData {
        return ListItemMlcData(
            label = categoryInfo.name.toDynamicString(),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
            action = DataActionWrapper(
                type = categoryInfo.code
            )
        )
    }

    private fun createTableBlockAccordionOrgData(faqItem: FaqItem): TableBlockAccordionOrgData {
        return TableBlockAccordionOrgData(
            heading = faqItem.question,
            items = listOf(
                TableItemVerticalMlcData(
                    titleWithParams = TextWithParametersData(
                        text = faqItem.answer.toDynamicString(),
                        parameters = faqItem.parameters.map { param ->
                            TextParameter(
                                type = param.type,
                                data = TextParameter.Data(
                                    alt = param.data?.alt?.toDynamicStringOrNull(),
                                    name = param.data?.name?.toDynamicStringOrNull(),
                                    resource = param.data?.resource?.toDynamicStringOrNull()
                                )
                            )
                        }
                    )
                )
            )
        )
    }

    sealed interface Navigation : NavigationPath {
        data object NavigateToSearch : Navigation
        data class NavigateToSelf(val categoryId: String) : Navigation
    }

}