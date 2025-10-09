package ua.gov.diia.ui_base.components.organism.qr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.qr.LinkQrShareOrg
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlcData
import ua.gov.diia.ui_base.components.molecule.message.PaginationMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.PaginationMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.generatePaginationMessageMlcMockData
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.organism.chip.CenterChipBlackTabsOrg
import ua.gov.diia.ui_base.components.organism.chip.CenterChipBlackTabsOrgData
import ua.gov.diia.ui_base.components.organism.chip.generateCenterChipBlackTab_Two_OrgMockData
import ua.gov.diia.ui_base.components.organism.chip.toUiModel
import ua.gov.diia.ui_base.components.organism.sharing.LinkSharingOrg
import ua.gov.diia.ui_base.components.organism.sharing.LinkSharingOrgData
import ua.gov.diia.ui_base.components.organism.sharing.LinkSharingOrgMocTypes
import ua.gov.diia.ui_base.components.organism.sharing.generateLinkSharingOrgMockData
import ua.gov.diia.ui_base.components.organism.sharing.toUIModel
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun LinkQrShareOrg(
    modifier: Modifier = Modifier,
    data: LinkQrShareOrgData,
    loader: Loader = Loader.create(),
    onUIAction: (UIAction) -> Unit
) {

    val selectedTab = data.centerChipBlackTabsOrg?.preselectedCode ?: "none"

    val isLoading = loader.isLoadingByComponent(UIActionKeysCompose.LINK_QR_SHARE_ORG)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .testTag(data.componentId.asString())
            .height(434.dp)
    ) {
        if (data.qrCodeOrg != null || data.linkSharingOrg != null || data.paginationMessageMlc != null || data.centerChipBlackTabsOrg != null) {
            Column(
                modifier = Modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(386.dp)
                    ) {
                        TridentLoaderAtm(modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    data.centerChipBlackTabsOrg?.let {
                        // CenterChipBlack   TabsOrgData.items are required to have size = 2 here
                        CenterChipBlackTabsOrg(data = it, onUIAction = onUIAction)
                    }


                    //If preselected code is equal to the code of the first item then show LinkSharingOrg
                    if (selectedTab == data.centerChipBlackTabsOrg?.items?.firstOrNull()?.code) {
                        data.linkSharingOrg?.let {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                            LinkSharingOrg(
                                data = it,
                                onUIAction = onUIAction
                            )
                        } ?: ErrorMessage(data, onUIAction = onUIAction)
                    }

                    //If preselected code is equal to the code of the second item then show QrCodeOrg
                    else if (selectedTab == data.centerChipBlackTabsOrg?.items?.getOrNull(1)?.code) {
                        data.qrCodeOrg?.let {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                            QrCodeOrg(data = it, onUIAction = onUIAction)
                        } ?: ErrorMessage(data, onUIAction = onUIAction)
                    } else {
                        ErrorMessage(data, onUIAction = onUIAction)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(
    data: LinkQrShareOrgData,
    onUIAction: (UIAction) -> Unit
) {
    if (data.paginationMessageMlc != null) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(386.dp)
        ) {
            val topAlpha = 71f / 386f
            val bottomAlpha = 109f / 386f

            val topPadding = maxHeight * topAlpha
            val bottomPadding = maxHeight * bottomAlpha

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding, bottom = bottomPadding),
                contentAlignment = Alignment.Center
            ) {
                PaginationMessageMlc(
                    data = data.paginationMessageMlc,
                    onUIAction = onUIAction,
                    modifier = Modifier
                        .wrapContentHeight(unbounded = true)
                        .defaultMinSize(minHeight = 0.dp)
                )
            }
        }
    }
}


data class LinkQrShareOrgData(
    val actionKey: String = UIActionKeysCompose.LINK_QR_SHARE_ORG,
    val componentId: UiText = UiText.DynamicString("linkQrShareOrg"),
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val centerChipBlackTabsOrg: CenterChipBlackTabsOrgData? = null,
    val linkSharingOrg: LinkSharingOrgData? = null,
    val qrCodeOrg: QrCodeOrgData? = null,
    val paginationMessageMlc: PaginationMessageMlcData? = null,
) : UIElementData

fun LinkQrShareOrg.toUiModel(): LinkQrShareOrgData {
    return LinkQrShareOrgData(
        componentId = UiText.DynamicString(componentId),
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        centerChipBlackTabsOrg = centerChipBlackTabsOrg?.toUiModel(),
        linkSharingOrg = linkSharingOrg?.toUIModel(),
        qrCodeOrg = qrCodeOrg?.toUIModel(),
        paginationMessageMlc = paginationMessageMlc?.toUIModel()
    )
}

@Composable
@Preview
fun LinkQrShareOrg_LinkSharingOrg_Preview() {
    LinkQrShareOrg(
        data = LinkQrShareOrgData(
            centerChipBlackTabsOrg = generateCenterChipBlackTab_Two_OrgMockData(preselectedCode = "1"),
            linkSharingOrg = generateLinkSharingOrgMockData(LinkSharingOrgMocTypes.WITH_TEXT),
            qrCodeOrg = QrCodeOrgData(
                text = "Text",
                componentId = "qrCodeOrg",
                qrCode = QrCodeMlcData(
                    qrLink = UiText.DynamicString("www.google.com")
                ),
                expireLabelFirst = "Код діятиме".toDynamicString(),
                expireLabelLast = "хв".toDynamicString(),
                timer = 10,
                paginationMessageMlc = generatePaginationMessageMlcMockData()
            ),
            paginationMessageMlc = generatePaginationMessageMlcMockData()
        )
    ) {}
}

@Composable
@Preview
fun LinkQrShareOrg_QrCodeOrg_Preview() {
    LinkQrShareOrg(
        data = LinkQrShareOrgData(
            paddingTop = TopPaddingMode.MEDIUM,
            paddingHorizontal = SidePaddingMode.MEDIUM,
            centerChipBlackTabsOrg = generateCenterChipBlackTab_Two_OrgMockData(preselectedCode = "2"),
            linkSharingOrg = generateLinkSharingOrgMockData(LinkSharingOrgMocTypes.WITH_TEXT),
            qrCodeOrg = QrCodeOrgData(
                text = "Text",
                componentId = "qrCodeOrg",
                qrCode = QrCodeMlcData(
                    qrLink = UiText.DynamicString("www.google.com")
                ),
                expireLabelFirst = "Код діятиме".toDynamicString(),
                expireLabelLast = "хв".toDynamicString(),
                timer = 10,
                paginationMessageMlc = generatePaginationMessageMlcMockData()
            ),
            paginationMessageMlc = generatePaginationMessageMlcMockData()
        )
    ) {}
}

@Composable
@Preview
fun LinkQrShareOrg_PaginationMessage_Preview() {
    LinkQrShareOrg(
        data = LinkQrShareOrgData(
            paddingTop = TopPaddingMode.MEDIUM,
            paddingHorizontal = SidePaddingMode.MEDIUM,
            centerChipBlackTabsOrg = generateCenterChipBlackTab_Two_OrgMockData(preselectedCode = "7"),
            linkSharingOrg = generateLinkSharingOrgMockData(LinkSharingOrgMocTypes.WITH_TEXT),
            qrCodeOrg = QrCodeOrgData(
                text = "Text",
                componentId = "qrCodeOrg",
                qrCode = QrCodeMlcData(
                    qrLink = UiText.DynamicString("www.google.com")
                ),
                expireLabelFirst = "Код діятиме".toDynamicString(),
                expireLabelLast = "хв".toDynamicString(),
                timer = 10,
                paginationMessageMlc = generatePaginationMessageMlcMockData()
            ),
            paginationMessageMlc = generatePaginationMessageMlcMockData()
        )
    ) {}
}

@Composable
@Preview
fun LinkQrShareOrg_Loading_Preview() {
    LinkQrShareOrg(
        loader = Loader.createComponent(UIActionKeysCompose.LINK_QR_SHARE_ORG, true),
        data = LinkQrShareOrgData(
            paddingTop = TopPaddingMode.MEDIUM,
            paddingHorizontal = SidePaddingMode.MEDIUM,
            centerChipBlackTabsOrg = generateCenterChipBlackTab_Two_OrgMockData(preselectedCode = "7"),
            linkSharingOrg = generateLinkSharingOrgMockData(LinkSharingOrgMocTypes.WITH_TEXT),
            qrCodeOrg = QrCodeOrgData(
                text = "Text",
                componentId = "qrCodeOrg",
                qrCode = QrCodeMlcData(
                    qrLink = UiText.DynamicString("www.google.com")
                ),
                expireLabelFirst = "Код діятиме".toDynamicString(),
                expireLabelLast = "хв".toDynamicString(),
                timer = 10,
                paginationMessageMlc = generatePaginationMessageMlcMockData()
            ),
            paginationMessageMlc = generatePaginationMessageMlcMockData()
        )
    ) {}
}