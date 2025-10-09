package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.WhiteMenuCardMlc
import ua.gov.diia.ui_base.components.molecule.card.WhiteMenuCardMlcData
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import ua.gov.diia.ui_base.models.orientation.Orientation

@Composable
fun AddDocOrg(
    modifier: Modifier = Modifier,
    data: AddDocOrgData,
    orientation: Orientation = Orientation.Portrait,
    onUIAction: (UIAction) -> Unit
) {
    when (orientation) {
        Orientation.Portrait -> AddDocOrgPortrait(
            modifier,
            data,
            onUIAction
        )

        Orientation.Landscape -> AddDocOrgLandscape(
            modifier,
            data,
            onUIAction
        )
    }
}

@Composable
fun AddDocOrgPortrait(modifier: Modifier, data: AddDocOrgData, onUIAction: (UIAction) -> Unit) {
    ConstraintLayout(
        modifier = modifier
            .aspectRatio(0.7F)
    ) {
        val (whiteMenuCardMlc1, whiteMenuCardMlc2) = createRefs()
        val centerGuideline = createGuidelineFromTop(0.5f)
        WhiteMenuCardMlc(
            modifier = Modifier
                .constrainAs(whiteMenuCardMlc1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(centerGuideline, margin = 4.dp)
                    height = Dimension.fillToConstraints
                }
                .clearAndSetSemantics {
                    contentDescription = data.addDoc.title
                }
                .focusable(),
            data = data.addDoc,
            onUIAction = {
                onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.ADD_DOC_ORG,
                        data = data.docType,
                        optionalId = data.position.toString()
                    )
                )
            }
        )
        WhiteMenuCardMlc(
            modifier = Modifier
                .constrainAs(whiteMenuCardMlc2) {
                    top.linkTo(centerGuideline, margin = 4.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .clearAndSetSemantics {
                    contentDescription = data.changePosition.title
                }
                .focusable(),
            data = data.changePosition,
            onUIAction = onUIAction
        )
    }
}

@Composable
fun AddDocOrgLandscape(
    modifier: Modifier = Modifier,
    data: AddDocOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WhiteMenuCardMlc(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            data = data.addDoc,
            onUIAction = {
                onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.ADD_DOC_ORG,
                        data = data.docType,
                        optionalId = data.position.toString()
                    )
                )
            }
        )

        Spacer(modifier = Modifier.size(16.dp))

        WhiteMenuCardMlc(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            data = data.changePosition,
            onUIAction = onUIAction
        )
    }

}

data class AddDocOrgData(
    val actionKey: String = UIActionKeysCompose.ADD_DOC_ORG,
    val addDoc: WhiteMenuCardMlcData,
    val changePosition: WhiteMenuCardMlcData,
    val docType: String,
    val position: Int
) : DocsCarouselItem, UIElementData

@Preview
@Composable
fun AddDocOrgPreview(
) {
    val addDoc = WhiteMenuCardMlcData(
        title = "Додати документ",
        icon = UiText.StringResource(R.drawable.ic_add)
    )
    val changePosition = WhiteMenuCardMlcData(
        title = "Змінити порядок документів",
        icon = UiText.StringResource(R.drawable.ic_doc_reorder)

    )
    val data = AddDocOrgData(
        addDoc = addDoc, changePosition = changePosition, docType = "error", position = 1
    )

    AddDocOrg(modifier = Modifier, data = data) {}
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun AddDocOrgLandscapePreview(
) {
    val addDoc = WhiteMenuCardMlcData(
        title = "Додати документ",
        icon = UiText.StringResource(R.drawable.ic_add)
    )
    val changePosition = WhiteMenuCardMlcData(
        title = "Змінити порядок документів",
        icon = UiText.StringResource(R.drawable.ic_doc_reorder)

    )
    val data = AddDocOrgData(
        addDoc = addDoc, changePosition = changePosition, docType = "error", position = 1
    )

    AddDocOrgLandscape(modifier = Modifier, data = data) {}
}