package ua.gov.diia.ui_base.components.organism.sharing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.sharing.LinkSharingOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.button.BtnIconPlaiGroupMockTypes
import ua.gov.diia.ui_base.components.molecule.button.BtnIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.button.generateBtnIconPlanMlcMockData
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.molecule.sharing.LinkSharingMlc
import ua.gov.diia.ui_base.components.molecule.sharing.LinkSharingMlcData
import ua.gov.diia.ui_base.components.molecule.sharing.LinkSharingMlcMockTypes
import ua.gov.diia.ui_base.components.molecule.sharing.generateLinkSharingMlcMockData
import ua.gov.diia.ui_base.components.molecule.sharing.toUIModel
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun LinkSharingOrg(
    modifier: Modifier = Modifier,
    data: LinkSharingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(332.dp)
            .testTag(data.componentId),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        data.text?.let { text ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                text = text,
                color = Black,
                style = DiiaTextStyle.t2TextDescription,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        } ?: Spacer(modifier = Modifier.height(48.dp))

        Column {
            LinkSharingMlc(data = data.linkSharing)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            DividerSlimAtom()
            data.description?.let { description ->
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = description,
                    color = BlackAlpha50,
                    style = DiiaTextStyle.t4TextSmallDescription,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            } ?: Spacer(modifier = Modifier.height(145.dp))
        }

        Box {
            data.btnIconPlainGroup?.let {
                BtnIconPlainGroupMlc(
                    data = data.btnIconPlainGroup,
                    onUIAction = { onUIAction(it) })
            }
        }

    }
}

data class LinkSharingOrgData(
    val componentId: String = "linkSharingOrg",
    val text: String? = null,
    val linkSharing: LinkSharingMlcData,
    val description: String?,
    val btnIconPlainGroup: BtnIconPlainGroupMlcData?
) : UIElementData

fun LinkSharingOrg.toUIModel(): LinkSharingOrgData {
    return LinkSharingOrgData(
        componentId = componentId,
        text = text,
        linkSharing = linkSharingMlc.toUIModel(),
        description = description,
        btnIconPlainGroup = btnIconPlainGroupMlc?.toUIModel()
    )
}

enum class LinkSharingOrgMocTypes {
    WITH_TEXT, NO_TEX
}

fun generateLinkSharingOrgMockData(type: LinkSharingOrgMocTypes): LinkSharingOrgData {
    return when (type) {
        LinkSharingOrgMocTypes.NO_TEX -> LinkSharingOrgData(
            text = null,
            linkSharing = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.PADDINGS),
            description = null,
            btnIconPlainGroup = generateBtnIconPlanMlcMockData(BtnIconPlaiGroupMockTypes.ONE)
        )

        LinkSharingOrgMocTypes.WITH_TEXT -> LinkSharingOrgData(
            text = "Дія Володимир Святославович  має підтвердити запит за посиланням Дія Володимир Святославович  має підтвердити запит за посиланням Дія Володимир Святославович  має підтвердити запит за посиланням",
            linkSharing = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.PADDINGS),
            description = "Посилання діятиме 24 години",
            btnIconPlainGroup = generateBtnIconPlanMlcMockData(BtnIconPlaiGroupMockTypes.ONE)
        )
    }

}

@Composable
@Preview(showBackground = true)
fun LinkSharingOrg_Preview() {
    LinkSharingOrg(
        data = generateLinkSharingOrgMockData(type = LinkSharingOrgMocTypes.WITH_TEXT)
    ) {}
}


@Composable
@Preview(showBackground = true)
fun LinkSharingOrg_Preview_no_text_no_description() {
    LinkSharingOrg(
        data = generateLinkSharingOrgMockData(type = LinkSharingOrgMocTypes.NO_TEX)
    ) {}
}