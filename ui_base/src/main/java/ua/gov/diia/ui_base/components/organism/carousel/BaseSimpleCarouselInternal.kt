package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.pager.DotNavigationAtm
import ua.gov.diia.ui_base.components.atom.pager.DotNavigationAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.card.BankingCardMlc
import ua.gov.diia.ui_base.components.molecule.card.BankingCardMlcData
import ua.gov.diia.ui_base.components.infrastructure.utils.centerOnFocusIfNeeded
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlc
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlc
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlc
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlc
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlcData
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlc
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseSimpleCarouselInternal(
    modifier: Modifier = Modifier,
    data: BaseSimpleCarouselInternalData,
    connectivityState: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        data.items.size
    }
    var position by remember { mutableStateOf(0) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                position = currentPage
            }
    }

    val localDensity = LocalDensity.current

    var cardHeight by remember {
        mutableStateOf(0.dp)
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPager(
                modifier = Modifier
                    .semantics {
                        role = Role.Carousel
                    },
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                ),
                pageSize = PageSize.Fill,
                pageSpacing = 10.dp,
                pageContent = { pageIndex ->
                    when (val card = data.items[pageIndex]) {
                        is BankingCardMlcData -> {
                            BankingCardMlc(
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    cardHeight = with(localDensity) { coordinates.size.height.toDp() }
                                },
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is HalvedCardMlcData -> {
                            HalvedCardMlc(
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        cardHeight =
                                            with(localDensity) { coordinates.size.height.toDp() }
                                    }
                                    .centerOnFocusIfNeeded(pageIndex, pagerState, coroutineScope)
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = "\u200B"
                                        role = Role.Button
                                    }
                                    .focusable(),
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is SmallNotificationMlcData -> {
                            SmallNotificationMlc(
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        cardHeight =
                                            with(localDensity) { coordinates.size.height.toDp() }
                                    }
                                    .centerOnFocusIfNeeded(pageIndex, pagerState, coroutineScope)
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = "\u200B"
                                        role = Role.Button
                                    }
                                    .focusable(),
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is ArticlePicAtmData -> {
                            ArticlePicAtm(
                                modifier = Modifier,
                                data = card,
                                inCarousel = true,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is ImageCardMlcData -> {
                            ImageCardMlc(
                                modifier = Modifier,
                                data = card,
                                inCarousel = true,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is ArticleVideoMlcData -> {
                            ArticleVideoMlc(
                                modifier = Modifier,
                                data = card,
                                inCarousel = true,
                                clickable = pageIndex == position,
                                connectivityState = connectivityState,
                                onUIAction = onUIAction
                            )
                        }

                        is IconCardMlcData -> {
                            val cardDescription = card.label.asString()
                            IconCardMlc(
                                modifier = Modifier
                                    .height(cardHeight)
                                    .centerOnFocusIfNeeded(pageIndex, pagerState, coroutineScope)
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = cardDescription
                                        role = Role.Button
                                    },
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
            )
        }

        if (data.items.isNotEmpty()) {
            when (data.items[position]) {
                is BankingCardMlcData -> {
                    onUIAction(
                        UIAction(
                            actionKey = "bankingCardCarouselScroll",
                            data = data.items[position].id,
                        )
                    )
                }
            }
        }

        if (data.items.size > 1) {
            DotNavigationAtm(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                data = DotNavigationAtmData(
                    activeDotIndex = position,
                    totalCount = data.items.size
                )
            )
        }
    }
}

interface BaseSimpleCarouselInternalData : UIElementData {
    val items: List<SimpleCarouselCard>
}

interface SimpleCarouselCard : UIElementData {
    val id: String?
}
