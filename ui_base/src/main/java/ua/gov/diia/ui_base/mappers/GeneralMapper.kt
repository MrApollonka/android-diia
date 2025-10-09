package ua.gov.diia.ui_base.mappers

import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.common_compose.general.Body
import ua.gov.diia.core.models.common_compose.general.BottomGroup
import ua.gov.diia.core.models.common_compose.general.CenteredBody
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.general.TopGroup
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlcV2
import ua.gov.diia.core.models.common_compose.mlc.header.TitleGroupMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.media.toUiModel
import ua.gov.diia.ui_base.components.atom.space.toUiModel
import ua.gov.diia.ui_base.components.atom.text.toUIModel
import ua.gov.diia.ui_base.components.atom.text.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.molecule.button.toUiModel
import ua.gov.diia.ui_base.components.molecule.card.toUIModel
import ua.gov.diia.ui_base.components.molecule.card.toUiModel
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.molecule.divider.toUiModel
import ua.gov.diia.ui_base.components.molecule.header.toUiModel
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUIModel
import ua.gov.diia.ui_base.components.molecule.input.toUiModel
import ua.gov.diia.ui_base.components.molecule.list.radio.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.toUiModel
import ua.gov.diia.ui_base.components.molecule.media.toUiModel
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.molecule.sharing.toUIModel
import ua.gov.diia.ui_base.components.molecule.text.toUIModel
import ua.gov.diia.ui_base.components.molecule.text.toUiModel
import ua.gov.diia.ui_base.components.organism.accordion.toUIModel
import ua.gov.diia.ui_base.components.organism.block.toUIModel
import ua.gov.diia.ui_base.components.organism.bottom.toUiModel
import ua.gov.diia.ui_base.components.organism.calendar.toUiModel
import ua.gov.diia.ui_base.components.organism.carousel.toUiModel
import ua.gov.diia.ui_base.components.organism.checkbox.toUIModel
import ua.gov.diia.ui_base.components.organism.chip.toUiModel
import ua.gov.diia.ui_base.components.organism.container.toUIModel
import ua.gov.diia.ui_base.components.organism.document.toUIModel
import ua.gov.diia.ui_base.components.organism.document.toUiModel
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.organism.input.toUIModel
import ua.gov.diia.ui_base.components.organism.input.toUiModel
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.components.organism.list.toUiModel
import ua.gov.diia.ui_base.components.organism.photo.toUiModel
import ua.gov.diia.ui_base.components.organism.qr.toUIModel
import ua.gov.diia.ui_base.components.organism.qr.toUiModel
import ua.gov.diia.ui_base.components.organism.radio.toUiModel
import ua.gov.diia.ui_base.components.organism.sharing.toUIModel
import ua.gov.diia.ui_base.components.organism.table.toUIModel
import ua.gov.diia.ui_base.components.organism.tile.toUIModel
import ua.gov.diia.ui_base.components.organism.toUIModel
import ua.gov.diia.ui_base.components.organism.toUiModel
import ua.gov.diia.ui_base.util.toUiModel
import ua.gov.diia.ui_base.util.toUiModellistItemGroupOrg

fun List<TopGroup>.getEllipseMenu(): List<ContextMenuItem>? {
    return this.firstOrNull {
        it.topGroupOrg != null
    }?.topGroupOrg?.navigationPanelMlc?.ellipseMenu
}

fun TopGroup.mapToComposeTopData(isContextMenuExist: Boolean): UIElementData? {
    this.topGroupOrg?.let {
        return it.toTopGroup(
            navigationPanelMlc = it.navigationPanelMlc,
            chipTabsOrgData = it.chipTabsOrg?.toUiModel(),
            isContextMenuExist = isContextMenuExist,
            titleGroupMlc = it.titleGroupMlc,
            navigationPanelMlcV2 = it.navigationPanelMlcV2
        )
    }
    this.searchInputMlc?.let {
        return it.toUiModel()
    }
    this.chipTabsOrg?.let {
        return it.toUiModel()
    }
    this.searchBarOrg?.let {
        return it.toUiModel()
    }
    this.mapChipTabsOrg?.let {
        return it.toUiModel()
    }
    this.scalingTitleMlc?.let {
        return it.toUiModel()
    }
    this.navigationPanelMlcV2?.let {
        return it.toUiModel()
    }
    return null
}

fun TopGroupOrg?.toTopGroup(
    actionKey: String = UIActionKeysCompose.TOP_GROUP_ORG,
    navigationPanelMlc: NavigationPanelMlc?,
    chipTabsOrgData: ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData? = null,
    searchInputMlcData: SearchInputMlcData? = null,
    searchBarOrgData: SearchBarOrgData? = null,
    isContextMenuExist: Boolean? = null,
    titleGroupMlc: TitleGroupMlc? = null,
    navigationPanelMlcV2: NavigationPanelMlcV2? = null,
): TopGroupOrgData? {
    if (this == null) return null
    return TopGroupOrgData(
        actionKey = actionKey,
        navigationPanelMlcData = navigationPanelMlc?.toUiModel(),
        titleGroupMlcData = titleGroupMlc?.toUiModel(),
        chipTabsOrgData = chipTabsOrgData,
        searchInputMlcData = searchInputMlcData,
        searchBarOrgData = searchBarOrgData,
        navigationPanelMlcV2Data = navigationPanelMlcV2?.toUiModel()
    )
}

// DiiaResponse.Body
fun Body.mapToComposeBodyData(pagination: ((PaddingMode?) -> UIElementData)? = null): UIElementData? {
    this.textLabelContainerMlc?.let {
        return it.toUiModel()
    }
    this.titleLabelMlc?.let {
        return it.toUIModel()
    }
    this.titleCentralizedMlc?.let {
        return it.toUIModel()
    }
    this.subTitleCentralizedMlc?.let {
        return it.toUiModel()
    }
    this.subtitleLabelMlc?.let {
        return it.toUiModel()
    }
    this.textLabelMlc?.let {
        return it.toUIModel()
    }
    this.attentionMessageMlc?.let {
        return it.toUIModel()
    }
    this.attentionIconMessageMlc?.let {
        return it.toUiModel()
    }
    this.stubMessageMlc?.let {
        return it.toUIModel()
    }
    this.statusMessageMlc?.let {
        return it.toUIModel()
    }
    this.tableBlockOrgV2?.let {
        return it.toUIModel()
    }
    this.tableBlockOrg?.let {
        return it.toUIModel()
    }
    this.tableBlockPlaneOrg?.let {
        return it.toUIModel()
    }
    this.tableBlockAccordionOrg?.let {
        return it.toUIModel()
    }
    this.fullScreenVideoOrg?.let {
        return it.toUIModel()
    }
    this.listItemGroupOrg?.let {
        return it.toUIModel()
    }
    this.cardMlc?.let {
        return it.toUIModel()
    }
    this.questionFormsOrg?.let {
        return it.toUIModel()
    }
    this.checkboxRoundGroupOrg?.let {
        return it.toUIModel()
    }
    this.radioBtnGroupOrg?.let {
        return it.toUIModel()
    }
    this.radioBtnGroupOrgV2?.let {
        return it.toUIModel()
    }
    this.btnPrimaryDefaultAtm?.let {
        return it.toUIModel()
    }
    this.btnStrokeDefaultAtm?.let {
        return it.toUIModel()
    }
    this.btnStrokeWhiteAtm?.let {
        return it.toUIModel()
    }
    this.btnPrimaryWideAtm?.let {
        return it.toUIModel()
    }
    this.btnPlainAtm?.let {
        return it.toUIModel()
    }
    this.checkboxBtnOrg?.let {
        return it.toUIModel()
    }
    this.spacerAtm?.let {
        return it.toUiModel()
    }
    this.mediaUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.singleMediaUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.fileUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.groupFilesAddOrg?.let {
        return it.toUiModel()
    }
    this.btnLoadIconPlainGroupMlc?.let {
        return it.toUIModel()
    }
    this.dividerLineMlc?.let {
        return it.toUiModel()
    }
    this.btnIconPlainGroupMlc?.let {
        return it.toUIModel()
    }
    this.tableBlockPlaneOrg?.let {
        return it.toUIModel()
    }
    this.paymentInfoOrg?.let {
        return it.toUiModel()
    }
    this.calendarOrg?.let {
        return it.toUiModel()
    }
    this.editAutomaticallyDeterminedValueOrg?.let {
        return it.toUIModel()
    }
    this.sharingCodesOrg?.let {
        return it.toUIModel()
    }
    this.radioBtnWithAltOrg?.let {
        return it.toUiModel()
    }
    this.alertCardMlc?.let {
        return it.toUiModel()
    }
    this.alertCardMlcV2?.let {
        return it.toUiModel()
    }
    this.dashboardCardTileOrg?.let {
        return it.toUIModel()
    }
    this.inputNumberLargeMlc?.let {
        return it.toUIModel()
    }
    this.articlePicAtm?.let {
        return it.toUiModel()
    }
    this.subtitleLabelMlc?.let {
        return it.toUiModel()
    }
    this.docHeadingOrg?.let {
        return it.toUiModel()
    }
    this.largeTickerAtm?.let {
        return it.toUiModel()
    }
    this.chipBlackGroupOrg?.let {
        return it.toUiModel()
    }
    this.chipTabsOrg?.let {
        return it.toUiModel()
    }
    this.inputPhoneCodeOrg?.let {
        return it.toUIModel()
    }
    this.paginationListWhiteOrg?.let {
        return pagination?.invoke(it.paddingMode)
    }
    this.paginationListOrg?.let {
        return pagination?.invoke(it.paddingMode)
    }
    this.checkboxBtnWhiteOrg?.let {
        return it.toUIModel()
    }
    this.smallCheckIconOrg?.let {
        return it.toUIModel()
    }
    this.photoGroupOrg?.let {
        return it.toUiModel()
    }
    this.imageCardCarouselOrg?.let {
        return it.toUiModel()
    }
    this.articleVideoMlc?.let {
        return it.toUiModel()
    }
    this.sectionTitleAtm?.let {
        return it.toUiModel()
    }
    this.cardMlcV2?.let {
        return it.toUIModel()
    }
    this.loopingVideoPlayerCardMlc?.let {
        return it.toUiModel()
    }
    this.bankingCardCarouselOrg?.let {
        return it.toUiModel()
    }
    this.updatedContainerOrg?.let {
        return it.toUIModel()
    }
    this.timerMlc?.let {
        return it.toUIModel()
    }
    this.btnWhiteLargeIconAtm?.let {
        return it.toUIModel()
    }
    this.titleLabelIconMlc?.let {
        return it.toUiModel()
    }
    this.backgroundWhiteOrg?.let {
        return it.toUIModel()
    }
    this.inputBlockOrg?.let {
        return it.toUIModel()
    }
    this.accordionOrg?.let {
        return it.toUIModel()
    }
    this.tableAccordionOrg?.let {
        return it.toUIModel()
    }
    this.paymentInfoOrgV2?.let {
        return it.toUiModel()
    }
    this.finalScreenBlockMlc?.let {
        return it.toUiModel()
    }
    this.itemReadMlc?.let {
        return it.toUiModel()
    }
    this.textBlockOrg?.let {
        return it.toUIModel()
    }
    this.greyTitleAtm?.let {
        return it.toUIModel()
    }
    this.accordionOrg?.let {
        return it.toUIModel()
    }
    this.tableAccordionOrg?.let {
        return it.toUIModel()
    }
    this.tableBlockOrgV2?.let {
        return it.toUIModel()
    }
    this.tableSecondaryHeadingMlc?.let {
        return it.toUIModel()
    }
    this.textBlockOrg?.let {
        return it.toUIModel()
    }
    this.greyTitleAtm?.let {
        return it.toUIModel()
    }
    this.outlineButtonOrg?.let {
        return it.toUIModel()
    }
    this.centerChipBlackTabsOrg?.let {
        return it.toUiModel()
    }
    this.linkSharingMlc?.let {
        return it.toUIModel()
    }
    this.linkSharingOrg?.let {
        return it.toUIModel()
    }
    this.qrCodeOrg?.let {
        return it.toUIModel()
    }
    this.linkSharingMlc?.let {
        return it.toUIModel()
    }
    this.linkSharingOrg?.let {
        return it.toUIModel()
    }
    this.centerChipBlackTabsOrg?.let {
        return it.toUiModel()
    }
    this.paymentInfoOrgV2?.let {
        return it.toUiModel()
    }
    this.finalScreenBlockMlc?.let {
        return it.toUiModel()
    }
    this.itemReadMlc?.let {
        return it.toUiModel()
    }
    this.textBlockOrg?.let {
        return it.toUIModel()
    }
    this.greyTitleAtm?.let {
        return it.toUIModel()
    }
    this.outlineButtonOrg?.let {
        return it.toUIModel()
    }
    this.stubInfoMessageMlc?.let {
        return it.toUiModel()
    }
    this.recursiveContainerOrg?.let {
        return it.toUiModel()
    }
    this.selectorOrg?.let {
        return it.toUIModel()
    }
    this.selectorOrgV2?.let {
        return it.toUIModel()
    }
    this.linkQrShareOrg?.let {
        return it.toUiModel()
    }
    this.tickerAtm?.let {
        return it.toUiModel()
    }
    this.paginationMessageMlc?.let {
        return it.toUIModel()
    }
    return null
}

fun CenteredBody.mapToComposeCenteredBodyData(): UIElementData? {
    this.finalScreenBlockMlc?.let {
        return it.toUiModel()
    }
    this.paymentInfoOrgV2?.let {
        return it.toUiModel()
    }
    return null
}

fun BottomGroup.mapToComposeBottomData(): UIElementData? {
    this.listItemGroupOrg?.let {
        return it.toUiModellistItemGroupOrg()
    }
    this.bottomGroupOrg?.let {
        return it.toUiModel()
    }
    this.tickerAtm?.let {
        return it.toUiModel()
    }
    this.btnPrimaryDefaultAtm?.let {
        return it.toUIModel()
    }
    this.btnPlainAtm?.let {
        return it.toUIModel()
    }
    this.btnSlideMlc?.let {
        return it.toUiModel()
    }
    return null
}