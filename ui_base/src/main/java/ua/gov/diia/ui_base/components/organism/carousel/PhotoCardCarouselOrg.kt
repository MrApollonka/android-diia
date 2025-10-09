package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.atom.pager.DotBlackNavigationAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.card.PhotoCardMlc
import ua.gov.diia.ui_base.components.molecule.card.PhotoCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.generateMockPhotoCardMlcData
import kotlin.math.absoluteValue

@Composable
fun PhotoCardCarouselOrg(
    modifier: Modifier = Modifier,
    data: PhotoCardCarouselOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val totalItems = data.items.size

    val initialPage = remember {
        Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % totalItems
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .testTag(data.componentId.orEmpty())
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 8.dp,
            pageContent = { pageIndex ->
                val actualIndex = pageIndex % totalItems

                val pageOffset =
                    ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction)
                        .absoluteValue
                        .coerceIn(0f, 1f)

                val isFocused = when {
                    pagerState.currentPage % totalItems == actualIndex -> true
                    pagerState.isScrollInProgress && pageOffset < 1f -> true
                    else -> false
                }

                PhotoCardMlc(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleY = lerp(1f, 0.9f, pageOffset)
                        },
                    data = data.items[actualIndex],
                    isFocused = isFocused,
                    onUIAction = onUIAction
                )
            }
        )
        if (totalItems > 1) {
            DotBlackNavigationAtm(
                pagerState = pagerState,
                totalPageCount = totalItems,
                onPageSelect = { page ->
                    coroutineScope.launch {
                        pagerState.scrollToPage(page)
                    }
                }
            )
        }
    }
}

data class PhotoCardCarouselOrgData(
    val componentId: String? = null,
    val items: List<PhotoCardMlcData>,
    val minSelection: Int,
    val maxSelection: Int
) : UIElementData {

    fun onCheckClicked(
        id: String,
        action: () -> Unit = {}
    ): PhotoCardCarouselOrgData {
        val updatedItems = items.map { photoCard ->
            if (
                photoCard.checkboxMlcData?.componentId == id &&
                photoCard.checkboxMlcData.interactionState == UIState.Interaction.Enabled
            ) {
                photoCard.onCheckClicked().also { photoCardMlcData ->
                    if (photoCardMlcData.checkboxMlcData?.selectionState == UIState.Selection.Selected) {
                        action()
                    }
                }
            } else {
                photoCard
            }
        }

        val finalItems = updatedItems.map { photoCard ->
            val isSelected =
                photoCard.checkboxMlcData?.selectionState == UIState.Selection.Selected
            val shouldDisable =
                !isSelected && selectedCount(updatedItems) >= maxSelection

            photoCard.updateInteractionState(
                newState = if (shouldDisable) UIState.Interaction.Disabled else UIState.Interaction.Enabled
            )
        }

        return this.copy(items = finalItems)
    }

    fun selectedCount(items: List<PhotoCardMlcData>): Int {
        return items.count { photoCard ->
            photoCard.checkboxMlcData?.selectionState == UIState.Selection.Selected
        }
    }

    fun selectedIds(): List<String> {
        return items
            .filter { photoCardMlcData ->
                photoCardMlcData.checkboxMlcData?.selectionState == UIState.Selection.Selected
            }
            .mapNotNull { photoCardMlcData ->
                photoCardMlcData.componentId
            }
    }

}

fun generateMockPhotoCardCarouselOrgData() = PhotoCardCarouselOrgData(
    items = List(10) {
        generateMockPhotoCardMlcData()
    },
    minSelection = 1,
    maxSelection = 10
)

@Preview
@Composable
private fun PhotoCardCarouselOrgPreview() {
    PhotoCardCarouselOrg(
        data = generateMockPhotoCardCarouselOrgData(),
        onUIAction = {
            /* no-op */
        }
    )
}