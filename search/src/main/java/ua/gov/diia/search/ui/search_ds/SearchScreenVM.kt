package ua.gov.diia.search.ui.search_ds

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.search.helper.SearchScreenNavigationHelper
import ua.gov.diia.search.models.SearchResult
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class SearchScreenVM @Inject constructor(
    private val withContextMenu: WithContextMenu<ContextMenuField>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val navigationHelper: SearchScreenNavigationHelper,
    private val searchScreenMapper: SearchScreenMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    SearchScreenNavigationHelper by navigationHelper,
    WithContextMenu<ContextMenuField> by withContextMenu,
    SearchScreenMapper by searchScreenMapper,
    DispatcherProvider by dispatcherProvider {

    private val _contentLoadedKey = MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL)
    private val _contentLoaded = MutableStateFlow(true)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key -> key to value }

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key -> key to value }

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

    private var navBarTitle: String? = null
    private var searchValue: String? = null

    private var _resultKey: String? = null
    private var _itemList: List<SearchableItem> = emptyList()

    sealed class SearchScreenNavigation : NavigationPath {
        data class NavBackWithResult(
            val result: SearchResult
        ) : SearchScreenNavigation()
    }

    fun setNavBarTitle(navBarTitle: String?) {
        this.navBarTitle = navBarTitle
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                onSearch(event.data)
            }

            UIActionKeysCompose.LIST_ITEM_MLC -> {
                event.action?.resource?.let { itemId ->
                    findResult(itemId)
                }
            }

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                if (event.data == null) return else when (event.data) {
                    UIActionKeysCompose.LIST_ITEM_MLC -> {
                        event.action?.resource?.let { itemId ->
                            findResult(itemId)
                        }
                    }
                }
            }
        }
    }

    fun getScreenContent(
        navBarTitle: String?,
        contextMenu: Array<ContextMenuField>?,
        searchPlaceholder: String?,
        data: List<SearchableItem>,
        resultKey: String,
        requestCode: String? = null //Optional for any actions
    ) {
        this.navBarTitle = navBarTitle
        setContextMenu(contextMenu)
        _resultKey = resultKey
        _itemList = data

        val context = mutableListOf<ContextMenuItem>()
        contextMenu?.forEach { item ->
            context.add(item as ContextMenuItem)
        }
        _topGroupData.addAllIfNotNull(
            toTopGroupOrg(
                title = navBarTitle,
                contextMenu = context
            ),
            toSearchBarOrg(
                searchPlaceholder = searchPlaceholder
            )
        )
        _bodyData.addIfNotNull(
            _itemList.toListItemGroup()
        )
    }

    private fun updateList(value: String?) {
        val newList = _itemList.filterByQuery(value)

        if (newList.isEmpty()) {
            clearBody()
            _bodyData.add(
                StubMessageMlcData(
                    icon = UiText.DynamicString("🤷‍♂️"),//"\uD83D\uDD90"
                    title = UiText.DynamicString("За цим запитом нічого не знайдено"),
                    description = TextWithParametersData(
                        text = UiText.DynamicString("Проте ви можете спробувати пошукати по іншим словам")
                    )
                )
            )
        } else {
            val isStubM = _bodyData.firstOrNull { it is StubMessageMlcData }
            if (isStubM == null) {
                _bodyData.findAndChangeFirstByInstance<ListItemGroupOrgData> {
                    it.copy(
                        itemsList = newList.toListItemGroup()?.itemsList ?: emptyList()
                    )
                }
            } else {
                clearBody()
                _bodyData.addIfNotNull(
                    if (newList.isEmpty()) _itemList.toListItemGroup() else newList.toListItemGroup()
                )
            }
        }
    }

    private fun onSearch(value: String?) {
        searchValue = value
        _topGroupData.findAndChangeFirstByInstance<SearchBarOrgData> { searchBar ->
            searchBar.onChange(value)
        }

        executeActionOnFlow(
            dispatcher = Dispatchers.IO
        ) {
            searchValue?.let {
                updateList(value)
            }
        }
    }

    private fun clearBody() {
        _bodyData.clear()
    }

    //------------- Data filtering -----------------------

    private fun List<SearchableItem>.filterByQuery(q: String?): List<SearchableItem> {
        if (q == null || !q.isStringValid()) return this
        val result = mutableListOf<Pair<Int, SearchableItem>>()

        this.forEach {
            val index = it.getDisplayTitle().indexOf(q, ignoreCase = true)
            if (index != -1) result.add(index to it)
        }
        return result.sortedBy { it.first }.map { it.second }.safeSubList(0, 200)
    }

    private fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
        this.subList(fromIndex, toIndex.coerceAtMost(this.size))

    //---------- Find result ----------------------

    private fun findResult(selectedQ: String) {
        val key = _resultKey ?: return
        val item = _itemList.findResult(selectedQ)
        val result = SearchResult(item, key)
        _navigation.tryEmit(
            SearchScreenNavigation.NavBackWithResult(result)
        )
    }

    private fun List<SearchableItem>.findResult(q: String): Any? = let { list ->
        list.find { item -> item match q }
    }

    private infix fun SearchableItem.match(q: String) = getQueryString() == q
}