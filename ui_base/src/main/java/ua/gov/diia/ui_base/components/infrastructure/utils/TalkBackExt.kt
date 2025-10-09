package ua.gov.diia.ui_base.components.infrastructure.utils

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import ua.gov.diia.ui_base.R

fun Modifier.centerOnFocusIfNeeded(
    pageIndex: Int,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
): Modifier = composed {
    val context = LocalContext.current

    LaunchedEffect(pageIndex == pagerState.currentPage) {
        if (
            context.isTalkBackEnabled() &&
            pageIndex == pagerState.currentPage
        ) {
            coroutineScope.launch {
                pagerState.scrollToPage(pageIndex)
            }
        }
    }

    this.onFocusChanged {
        if (
            it.isFocused &&
            pageIndex != pagerState.currentPage &&
            context.isTalkBackEnabled()
        ) {
            coroutineScope.launch {
                pagerState.scrollToPage(pageIndex)
            }
        }
    }
}

fun Context.isTalkBackEnabled(): Boolean {
    val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
    return accessibilityManager?.isEnabled == true && accessibilityManager.isTouchExplorationEnabled
}

fun Modifier.cardAccessibilityAndFocusModifier(
    page: Int,
    state: PagerState,
    coroutineScope: CoroutineScope,
    isFullCardFocus: MutableState<Boolean>,
    cardTitle: String = "",
    context: Context
): Modifier {
    return this
        .semantics {
            customActions = listOfNotNull(
                if (!isFullCardFocus.value) {
                    CustomAccessibilityAction(
                        label = context.getString(R.string.accessibility_enable_switching_between_documents),
                        action = {
                            isFullCardFocus.value = true
                            true
                        }
                    )
                } else {
                    CustomAccessibilityAction(
                        label = context.getString(R.string.accessibility_enable_switching_inside_the_document),
                        action = {
                            isFullCardFocus.value = false
                            true
                        }
                    )
                }
            )
        }
        .centerOnFocusIfNeeded(page, state, coroutineScope)
        .then(
            if (!isFullCardFocus.value) {
                if (page != state.currentPage) {
                    Modifier.clearAndSetSemantics {}
                } else {
                    Modifier
                }
            } else Modifier.clearAndSetSemantics {
                contentDescription = cardTitle
            }
        )
}