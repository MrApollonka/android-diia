package ua.gov.diia.search.ui.search_ds

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import javax.inject.Inject

interface SearchScreenMapper {

    fun toTopGroupOrg(
        title: String?,
        contextMenu: List<ContextMenuItem>?
    ): TopGroupOrgData?

    fun toSearchBarOrg(
        searchPlaceholder: String? = null
    ): SearchBarOrgData?

    fun List<SearchableItem>.toListItemGroup(): ListItemGroupOrgData?
}

class SearchScreenMapperImpl @Inject constructor() : SearchScreenMapper {

    override fun toTopGroupOrg(
        title: String?,
        contextMenu: List<ContextMenuItem>?,
    ): TopGroupOrgData? {
        return TopGroupOrgData(
            navigationPanelMlcData = NavigationPanelMlcData(
                title = UiText.DynamicString(title ?: "Екран пошуку"),
                isContextMenuExist = contextMenu?.isNotEmpty() ?: false
            )
        )
    }

    override fun toSearchBarOrg(searchPlaceholder: String?): SearchBarOrgData? {
        return SearchBarOrgData(
            componentId = UiText.DynamicString("searchInput"),
            searchInputMlc = SearchInputMlcData(
                id = "searchInput",
                placeholder = UiText.DynamicString(searchPlaceholder ?: "Пошук"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
                iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
            ),
            actionKey = UIActionKeysCompose.SEARCH_INPUT,
            btnWhiteAdditionalIconAtm = null
        )
    }

    override fun List<SearchableItem>.toListItemGroup(): ListItemGroupOrgData? {
        val list = this
        return ListItemGroupOrgData(
            componentId = "ListItemGroupOrg".toDynamicStringOrNull(),
            title = null,
            itemsList = SnapshotStateList<ListItemMlcData>().apply {
                list.forEachIndexed { index, item ->
                    add(item.toListItemMlc())
                }
            },
            button = null
        )
    }

    private fun SearchableItem.toListItemMlc(): ListItemMlcData {
        return ListItemMlcData(
            componentId = UiText.DynamicString(UIActionKeysCompose.LIST_ITEM_MLC),
            id = UIActionKeysCompose.LIST_ITEM_MLC,
            label = this.getDisplayTitle().toDynamicString(),
            action = DataActionWrapper(
                type = this.getDisplayTitle(),
                resource = this.getQueryString()
            ),
            interactionState = if (this.isDisabled()) UIState.Interaction.Disabled else UIState.Interaction.Enabled,
        )
    }
}