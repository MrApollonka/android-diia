package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcLandscape
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.organism.pager.CardFocus
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import ua.gov.diia.ui_base.components.theme.WhiteAlpha25
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40
import ua.gov.diia.ui_base.models.orientation.Orientation
import java.util.UUID

@Composable
fun DocOrg(
    modifier: Modifier = Modifier,
    data: DocOrgData,
    cardFocus: CardFocus = CardFocus.UNDEFINED,
    orientation: Orientation = Orientation.Portrait,
    onUIAction: (UIAction) -> Unit
) {
    val cashedData = remember { mutableStateOf(data) }
    val imageDisplayed = remember { mutableStateOf(cardFocus != CardFocus.OUT_OF_FOCUS) }

    LaunchedEffect(key1 = cardFocus) {
        imageDisplayed.value = cardFocus != CardFocus.OUT_OF_FOCUS
    }

    when (orientation) {
        Orientation.Portrait -> DocOrgPortrait(modifier, cashedData, imageDisplayed, onUIAction)
        Orientation.Landscape -> DocOrgLandscape(modifier, cashedData, imageDisplayed, onUIAction)
    }
}

@Composable
fun DocOrgPortrait(
    modifier: Modifier,
    cashedData: MutableState<DocOrgData>,
    imageDisplayed: MutableState<Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .aspectRatio(0.7F)
            .background(color = WhiteAlpha40, shape = RoundedCornerShape(24.dp))
    ) {
        val (image, chipStatusAtm, docHeading, docButtonHeading, docCover, stackBackground) = createRefs()

        AsyncImage(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .fillMaxWidth()
                .aspectRatio(0.7F)
                .alpha(if (imageDisplayed.value) 1f else 0f)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            model = cashedData.value.imageUrl,
            contentDescription = "\u200B",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(cashedData.value.placeHolder),
            error = painterResource(cashedData.value.placeHolder)
        )

        cashedData.value.chipStatusAtmData?.let {
            ChipStatusAtm(
                modifier = Modifier
                    .constrainAs(chipStatusAtm) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                data = it
            )
        }

        cashedData.value.docHeading?.let {
            DocHeadingOrg(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (cashedData.value.chipStatusAtmData != null) 8.dp else 20.dp,
                        end = 16.dp
                    )
                    .constrainAs(docHeading) {
                        top.linkTo(if (cashedData.value.chipStatusAtmData != null) chipStatusAtm.bottom else parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                data = it,
                onUIAction = onUIAction
            )
        }

        cashedData.value.docButtonHeading?.let {
            DocButtonHeadingOrg(
                modifier = Modifier
                    .constrainAs(docButtonHeading) {
                        bottom.linkTo(parent.bottom, margin = 28.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                data = it,
                onUIAction = {
                    onUIAction(
                        UIAction(
                            actionKey = it.actionKey,
                            data = cashedData.value.docType,
                            optionalId = cashedData.value.position.toString()
                        )
                    )
                }
            )
        }

        if (cashedData.value.docCover != null && cashedData.value.isCurrentPage) {
            DocCoverMlc(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(docCover) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                data = cashedData.value.docCover!!,
                onUIAction = onUIAction
            )
        }

        if (cashedData.value.docButtonHeading?.isStack == true) {
            Column(
                modifier = modifier
                    .constrainAs(stackBackground) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .background(
                        color = WhiteAlpha25,
                        shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {}
        }
    }
}

@Composable
fun DocOrgLandscape(
    modifier: Modifier,
    cashedData: MutableState<DocOrgData>,
    imageDisplayed: MutableState<Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxHeight()
            .background(color = WhiteAlpha40, shape = RoundedCornerShape(24.dp))
    ) {
        val (image, chipStatusAtm, docHeading, docButtonHeading, docCover) = createRefs()

        AsyncImage(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .fillMaxWidth(fraction = 0.5f)
                .alpha(if (imageDisplayed.value) 1f else 0f)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            model = cashedData.value.imageUrl,
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )

        cashedData.value.chipStatusAtmData?.let {
            ChipStatusAtm(
                modifier = Modifier
                    .constrainAs(chipStatusAtm) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    },
                data = it
            )
        }

        cashedData.value.docHeading?.let {
            DocHeadingOrgLandscape(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(
                        top = if (cashedData.value.chipStatusAtmData != null) 16.dp else 20.dp,
                        end = 16.dp,
                        start = 16.dp
                    )
                    .constrainAs(docHeading) {
                        top.linkTo(if (cashedData.value.chipStatusAtmData != null) chipStatusAtm.bottom else parent.top)
                        start.linkTo(parent.start)
                    },
                data = it,
                onUIAction = onUIAction
            )
        }

        cashedData.value.docButtonHeading?.let {
            DocButtonHeadingOrgLandscape(
                modifier = Modifier
                    .constrainAs(docButtonHeading) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = 16.dp),
                data = it,
                onUIAction = {
                    onUIAction(
                        UIAction(
                            actionKey = it.actionKey,
                            data = cashedData.value.docType,
                            optionalId = cashedData.value.position.toString()
                        )
                    )
                }
            )
        }

        if (cashedData.value.docCover != null && cashedData.value.isCurrentPage) {
            DocCoverMlcLandscape(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .constrainAs(docCover) {
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                data = cashedData.value.docCover!!,
                onUIAction = onUIAction
            )
        }
    }
}

/**
 * @param id is set to be a unique key in case DocData is used in Pager or Lazy Colum
 */
data class DocOrgData(
    val id: String?,
    val actionKey: String = UIActionKeysCompose.DOC_ORG_DATA,
    val imageUrl: String,
    val docHeading: DocHeadingOrgData? = null,
    val docCover: DocCoverMlcData? = null,
    val docButtonHeading: DocButtonHeadingOrgData? = null,
    val docType: String,
    val position: Int,
    val isCurrentPage: Boolean = false,
    val chipStatusAtmData: ChipStatusAtmData? = null,
    val placeHolder: Int
) : DocsCarouselItem, UIElementData

@Preview
@Composable
fun DocOrgDataPreview() {
    val prevData = DocOrgData(
        id = UUID.randomUUID().toString(),
        actionKey = "sasd",
        imageUrl = "https://api2.diia.gov.ua/diia-images/award/gold-logo.png",
        docHeading = DocHeadingOrgData(
            ":bnkjdbq",
            heading = HeadingWithSubtitlesMlcData(value = "djsfjk", subtitles = listOf())
        ),
        docCover = null,
        docType = "dsadasd",
        position = 2,
        placeHolder = R.drawable.diia_article_placeholder
    )

    DocOrg(
        modifier = Modifier,
        data = prevData
    ) {
        /* no-op */
    }
}


@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocOrgDataLandscapePreview() {
    val prevData = DocOrgData(
        id = UUID.randomUUID().toString(),
        actionKey = "sasd",
        imageUrl = "https://api2.diia.gov.ua/diia-images/award/gold-logo.png",
        docHeading = DocHeadingOrgData(
            ":bnkjdbq",
            heading = HeadingWithSubtitlesMlcData(value = "djsfjk", subtitles = listOf())
        ),
        docCover = null,
        docType = "dsadasd",
        position = 2,
        placeHolder = R.drawable.diia_article_placeholder
    )

    DocOrg(
        modifier = Modifier,
        data = prevData,
        orientation = Orientation.Landscape
    ) {
        /* no-op */
    }
}