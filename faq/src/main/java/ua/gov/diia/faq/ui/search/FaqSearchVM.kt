package ua.gov.diia.faq.ui.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.faq.repository.FaqRepository
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.input.Mode
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.table.TableBlockAccordionOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class FaqSearchVM @Inject constructor(
    private val faqRepository: FaqRepository,
    private val withErrorHandlingOnFlow: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
) : ViewModel(),
    WithRetryLastAction by withRetryLastAction,
    WithErrorHandlingOnFlow by withErrorHandlingOnFlow {

    private val _top = mutableStateListOf<UIElementData>()
    val top: SnapshotStateList<UIElementData> = _top

    private val _body = mutableStateListOf<UIElementData>()
    val body: SnapshotStateList<UIElementData> = _body

    private val _navigationEvent = MutableSharedFlow<NavigationPath>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val queryStateFlow = MutableStateFlow("")

    init {
        createAndAddToolbarItem()
        createAndAddSearchItem()
        observeSearchQueryChange()
    }

    fun onUIAction(action: UIAction) {
        when (action.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                viewModelScope.launch {
                    _navigationEvent.emit(BaseNavigation.Back)
                }
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                onSearch(action.data)
            }
        }
    }

    private fun onSearch(value: String?) {
        _body.findAndChangeFirstByInstance<SearchInputV2Data> { searchBar ->
            searchBar.onChange(value)
        }
        queryStateFlow.update { value.orEmpty() }
    }

    private fun createAndAddToolbarItem() {
        _top.addAllIfNotNull(
            NavigationPanelMlcData(
                title = UiText.DynamicString("Питання та відповіді"),
                isContextMenuExist = false
            )
        )
    }

    private fun createAndAddSearchItem() {
        _body.add(
            SearchInputV2Data(
                placeholder = UiText.DynamicString("Що шукаєте?"),
                mode = Mode.EDITABLE.value
            )
        )
    }

    private fun observeSearchQueryChange() {
        viewModelScope.launch {
            combine(
                faqRepository.data.filterNotNull(),
                queryStateFlow
            ) { faq, query ->
                if (query.isBlank()) return@combine emptyList()

                val filteredItems = faq.categories
                    .flatMap { category -> category.faq }
                    .filter { faqItem -> faqItem.searchText.contains(query, ignoreCase = true) }

                if (filteredItems.isNotEmpty()) {
                    filteredItems.map { item ->
                        TableBlockAccordionOrgData(
                            heading = item.question,
                            items = listOf(
                                TableItemVerticalMlcData(
                                    title = UiText.DynamicString(item.answer)
                                )
                            )
                        )
                    }
                } else {
                    listOf(
                        StubMessageMlcData(
                            icon = UiText.DynamicString("\uD83E\uDD37\u200D♂️"),
                            title = UiText.DynamicString("Не знайдено жодної питання")
                        )
                    )
                }
            }.collect { items ->
                _body.removeRange(1, _body.size)
                _body.addAllIfNotNull(*items.toTypedArray())
            }
        }
    }

}