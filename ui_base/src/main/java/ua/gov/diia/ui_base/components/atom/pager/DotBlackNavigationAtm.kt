package ua.gov.diia.ui_base.components.atom.pager

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.Transparent
import kotlin.math.abs
import kotlin.math.min

@Composable
fun DotBlackNavigationAtm(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    totalPageCount: Int,
    onPageSelect: (Int) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val lazyListState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val actualCurrentPage = remember(pagerState.currentPage, totalPageCount) {
        (pagerState.currentPage % totalPageCount + totalPageCount) % totalPageCount
    }

    val firstVisibleItemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    var dragEnabled by remember { mutableStateOf(false) }

    val accumulatedDragAmount = remember { mutableFloatStateOf(0f) }

    val threshold = with(LocalDensity.current) { 16.dp.toPx() }

    LaunchedEffect(actualCurrentPage, totalPageCount) {
        coroutineScope.launch {
            if (totalPageCount <= MAX_VISIBLE_DOTS) {
                lazyListState.scrollToItem(0)
                return@launch
            }

            val absoluteDotStartStopIndex = firstVisibleItemIndex + DOT_START_STOP_INDEX
            val absoluteDotEndStopIndex = firstVisibleItemIndex + DOT_END_STOP_INDEX

            if (actualCurrentPage in absoluteDotStartStopIndex..absoluteDotEndStopIndex) {
                return@launch
            }

            val targetScrollIndex: Int = if (actualCurrentPage < absoluteDotStartStopIndex) {
                (actualCurrentPage - DOT_START_STOP_INDEX).coerceAtLeast(0)
            } else {
                (actualCurrentPage - DOT_END_STOP_INDEX).coerceAtLeast(0)
            }

            val maxPossibleScrollIndex = (totalPageCount - MAX_VISIBLE_DOTS).coerceAtLeast(0)
            val finalTargetScrollIndex = targetScrollIndex.coerceIn(0, maxPossibleScrollIndex)

            if (finalTargetScrollIndex in 0..<totalPageCount) {
                if (finalTargetScrollIndex != firstVisibleItemIndex) {
                    lazyListState.animateScrollToItem(index = finalTargetScrollIndex)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier
                .width(
                    DOT_ELEMENT_CONTAINER_SIZE * min(totalPageCount, MAX_VISIBLE_DOTS)
                )
                .background(
                    color = if (dragEnabled) BlackAlpha10 else Transparent,
                    shape = RoundedCornerShape(50)
                )
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            accumulatedDragAmount.floatValue = 0f
                            dragEnabled = true
                        },
                        onDrag = { change, dragAmount ->
                            if (dragEnabled) {
                                change.consume()
                                accumulatedDragAmount.floatValue += dragAmount.x

                                if (abs(accumulatedDragAmount.floatValue) >= threshold) {
                                    val nextPage = if (accumulatedDragAmount.floatValue < 0) {
                                        (actualCurrentPage - 1 + totalPageCount) % totalPageCount
                                    } else {
                                        (actualCurrentPage + 1) % totalPageCount
                                    }

                                    if (nextPage != actualCurrentPage) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                                        val currentBase = pagerState.currentPage - actualCurrentPage
                                        val newPage = currentBase + nextPage

                                        onPageSelect(newPage)
                                    }

                                    accumulatedDragAmount.floatValue = 0f
                                }
                            }
                        },
                        onDragEnd = {
                            accumulatedDragAmount.floatValue = 0f
                            dragEnabled = false
                        }
                    )
                },
            userScrollEnabled = false,
            state = lazyListState,
            horizontalArrangement = Arrangement.Center
        ) {
            val items = List(totalPageCount) { it }

            itemsIndexed(items) { index, _ ->
                val activeVisibleIndex = actualCurrentPage - firstVisibleItemIndex
                val visibleDotIndex = index - firstVisibleItemIndex

                val dotSize = calculateDotSize(
                    dotsCount = items.size.coerceAtMost(MAX_VISIBLE_DOTS),
                    dotIndex = visibleDotIndex,
                    activeDotIndex = activeVisibleIndex
                )
                val isActive = actualCurrentPage == index

                SingleDot(
                    dotSize = dotSize,
                    isActive = isActive
                )
            }
        }
    }
}

private fun calculateDotSize(
    dotsCount: Int,
    dotIndex: Int,
    activeDotIndex: Int
): Dp {
    val sizes = when (dotsCount) {
        6 -> when (activeDotIndex) {
            0, 1 -> listOf(8, 8, 8, 6, 6, 4)
            2 -> listOf(6, 8, 8, 8, 6, 4)
            3 -> listOf(4, 6, 8, 8, 8, 6)
            4, 5 -> listOf(4, 6, 6, 8, 8, 8)
            else -> List(6) { 8 }
        }

        5 -> when (activeDotIndex) {
            0, 1 -> listOf(8, 8, 8, 6, 6)
            2 -> listOf(6, 8, 8, 8, 6)
            3, 4 -> listOf(6, 6, 8, 8, 8)
            else -> List(5) { 8 }
        }

        4 -> when (activeDotIndex) {
            0, 1 -> listOf(8, 8, 8, 6)
            2, 3 -> listOf(6, 8, 8, 8)
            else -> List(4) { 8 }
        }

        3 -> when (activeDotIndex) {
            0 -> listOf(8, 8, 6)
            1 -> listOf(8, 8, 8)
            2 -> listOf(6, 8, 8)
            else -> List(3) { 8 }
        }

        2 -> listOf(8, 8)

        else -> List(dotsCount) { 8 }
    }

    return sizes.getOrElse(dotIndex) { 8 }.dp
}

@Composable
private fun SingleDot(
    dotSize: Dp,
    isActive: Boolean
) {
    val animatedSize by animateDpAsState(
        targetValue = dotSize,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedColor by animateColorAsState(
        targetValue = if (isActive) Black else BlackAlpha30,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .size(DOT_ELEMENT_CONTAINER_SIZE)
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(animatedSize)
                .clip(CircleShape)
                .background(animatedColor)
        )
    }
}

private val DOT_ELEMENT_CONTAINER_SIZE = 16.dp
private const val MAX_VISIBLE_DOTS = 6
private const val DOT_START_STOP_INDEX = 1
private const val DOT_END_STOP_INDEX = 4