package ua.gov.diia.publicservice.ui.categories.compose

import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceTab
import ua.gov.diia.publicservice.models.PublicServicesCategories
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrgData
import ua.gov.diia.ui_base.components.organism.carousel.HalvedCardCarouselOrgData

interface PublicServicesCategoriesTabMapper {

    fun generateServiceCardTileOrgData(
        categories: List<PublicServiceCategory>,
        tabs: List<PublicServiceTab>?,
        selectedTab: String?
    ): ServiceCardTileOrgData

    fun generateComposeChipTabBarV2(
        tabs: List<PublicServiceTab>?,
        selectedTab: String?
    ): ChipTabsOrgData?

    fun generateSearchInputMoleculeV2(
        placeholder: String,
        mode: Int,
    ): SearchInputV2Data?

    fun generateHalvedCardCarouselOrgData(
        selectedTab: String?,
        additionalElements: List<PublicServicesCategories.AdditionalElement>
    ): HalvedCardCarouselOrgData?

}