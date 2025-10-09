package ua.gov.diia.ui_base.components.organism.pager

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import ua.gov.diia.ui_base.components.atom.pager.DocDotNavigationAtm
import ua.gov.diia.ui_base.components.atom.pager.DocDotNavigationAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.cardAccessibilityAndFocusModifier
import ua.gov.diia.ui_base.components.organism.document.AddDocOrg
import ua.gov.diia.ui_base.components.organism.document.AddDocOrgData
import ua.gov.diia.ui_base.components.organism.document.DocActivateCardOrg
import ua.gov.diia.ui_base.components.organism.document.DocActivateCardOrgData
import ua.gov.diia.ui_base.components.organism.document.DocOrg
import ua.gov.diia.ui_base.components.organism.document.DocOrgData
import ua.gov.diia.ui_base.models.orientation.Orientation
import java.util.UUID
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseCarouselOrg(
    modifier: Modifier = Modifier,
    data: BaseCarouselOrgData,
    onUIAction: (UIAction) -> Unit,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    pageCount: Int
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    val screenDensity = LocalDensity.current.density

    val screenWidthPx = (screenWidthDp * screenDensity).toInt()
    val screenHeightPx = (screenHeightDp * screenDensity).toInt()

    val screenDiagonalInches = kotlin.math.sqrt(
        (screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx).toDouble()
    ) / (160 * screenDensity)

    val orientation = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Orientation.Landscape
    } else {
        Orientation.Portrait
    }

    var fraction by remember {
        mutableFloatStateOf(
            when {
                screenDiagonalInches < 5.10 -> 0.9f
                screenDiagonalInches > 5.10 && screenDiagonalInches < 5.25 -> 0.75f
                else -> 0.7f
            }
        )
    }

    val screenHeightInPixels = with(LocalDensity.current) { screenHeightDp.dp.toPx() }
    val desiredHeightInPixels = screenHeightInPixels * fraction
    val actualFraction = if (orientation == Orientation.Portrait)
        desiredHeightInPixels / screenHeightInPixels
    else 0.9f

    val state = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        pageCount
    }

    var scrollInProgress = remember { mutableStateOf(false) }
    var isFullCardFocus = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow {
            state.isScrollInProgress
        }.collect {
            scrollInProgress.value = it
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.targetPage }.collectLatest { page ->
            onUIAction.invoke(
                UIAction(actionKey = UIActionKeysCompose.DOC_PAGE_SELECTED, data = page.toString())
            )
        }
    }

    LaunchedEffect(key1 = state.currentPage) {
        onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_CARD_SWIPE_FINISHED))
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(actualFraction)
                    .semantics {
                        role = Role.Carousel
                    },
                state = state,
                pageSpacing = (16).dp,
                key = { "${UUID.randomUUID()}" },
                contentPadding = PaddingValues(
                    start = 32.dp,
                    end = 32.dp,
                    top = if (orientation == Orientation.Landscape) 16.dp else 0.dp,
                    bottom = if (orientation == Orientation.Landscape) 16.dp else 0.dp
                )
            ) { page ->
                val card = data.card[page]
                val docModifier = Modifier
                    .graphicsLayer {
                        val pageOffset =
                            ((state.currentPage - page) + state.currentPageOffsetFraction).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = 1f
                            scaleY = scale
                        }
                    }
                when (card) {
                    is DocCardFlipData -> {
                        val cardTitle = card.front.docHeading?.heading?.value ?: ""
                        DocCardFlip(
                            modifier = docModifier
                                .cardAccessibilityAndFocusModifier(
                                    page = page,
                                    state = state,
                                    coroutineScope = coroutineScope,
                                    isFullCardFocus = isFullCardFocus,
                                    cardTitle = cardTitle,
                                    context = context
                                ),
                            data = card,
                            progressIndicator = progressIndicator,
                            orientation = orientation,
                            cardFocus = if (scrollInProgress.value) {
                                CardFocus.UNDEFINED
                            } else {
                                if (state.currentPage == page) {
                                    CardFocus.IN_FOCUS
                                } else {
                                    CardFocus.OUT_OF_FOCUS
                                }

                            }
                        ) {
                            if (state.currentPage == page) {
                                onUIAction(
                                    UIAction(
                                        data = it.data,
                                        optionalId = page.toString(),
                                        actionKey = it.actionKey,
                                        action = it.action
                                    )
                                )
                            }
                        }
                    }

                    is DocOrgData -> {
                        val cardTitle = card.docHeading?.heading?.value ?: ""
                        DocOrg(
                            modifier = docModifier
                                .cardAccessibilityAndFocusModifier(
                                    page = page,
                                    state = state,
                                    coroutineScope = coroutineScope,
                                    isFullCardFocus = isFullCardFocus,
                                    cardTitle = cardTitle,
                                    context = context
                                ),
                            data = card,
                            orientation = orientation,
                            cardFocus = if (scrollInProgress.value) {
                                CardFocus.UNDEFINED
                            } else {
                                if (state.currentPage == page) {
                                    CardFocus.IN_FOCUS
                                } else {
                                    CardFocus.OUT_OF_FOCUS
                                }

                            }
                        ) {
                            onUIAction(
                                UIAction(
                                    data = it.data,
                                    optionalId = page.toString(),
                                    actionKey = it.actionKey
                                )
                            )
                        }
                    }

                    is AddDocOrgData -> {
                        AddDocOrg(
                            modifier = docModifier
                                .cardAccessibilityAndFocusModifier(
                                    page = page,
                                    state = state,
                                    coroutineScope = coroutineScope,
                                    isFullCardFocus = isFullCardFocus,
                                    context = context
                                ),
                            orientation = orientation,
                            data = card,
                            onUIAction = onUIAction
                        )
                    }

                    is DocActivateCardOrgData -> {
                        DocActivateCardOrg(
                            modifier = docModifier
                                .cardAccessibilityAndFocusModifier(
                                    page = page,
                                    state = state,
                                    coroutineScope = coroutineScope,
                                    isFullCardFocus = isFullCardFocus,
                                    context = context
                                ),
                            data = card,
                            onUIAction = onUIAction
                        )
                    }
                }
            }
        }
        DocDotNavigationAtm(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(if (state.pageCount > 1) 1f else 0f),
            data = DocDotNavigationAtmData(
                activeDotIndex = state.currentPage,
                totalCount = pageCount
            )
        )
    }

    LaunchedEffect(key1 = data.focusOnDoc, key2 = state.pageCount, block = {
        if (state.pageCount <= 0) return@LaunchedEffect
        if (data.focusOnDoc != null) {
            if (data.focusOnDoc >= state.pageCount && data.focusOnDoc != 0) {
                //scroll to last
                state.scrollToPage(state.pageCount - 1)
            } else {
                state.scrollToPage(data.focusOnDoc)
            }
        }
    })
}

data class BaseCarouselOrgData(val card: List<DocsCarouselItem>, val focusOnDoc: Int? = null)

enum class CardFocus {
    OUT_OF_FOCUS, IN_FOCUS, UNDEFINED
}