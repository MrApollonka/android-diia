package ua.gov.diia.ui_base.components.organism.list

import android.annotation.SuppressLint
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemDragMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemDragMlcData

@Composable
fun ListItemDragOrg(
    modifier: Modifier = Modifier,
    data: ListItemDragOrgData,
    onMove: (Int, Int) -> Unit,
    onUIAction: (UIAction) -> Unit,
) {
    val state = rememberReorderableLazyListState(
        onMove = { a, b -> onMove.invoke(a.index, b.index) },
        canDragOver = { _, _ -> true },
    )

    val context = LocalContext.current
    val view = LocalView.current
    var accessibilityFocusKey by remember { mutableStateOf<Any?>(null) }
    val fallbackFocusKey = data.items.firstOrNull()?.id

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .reorderable(state)
    ) {                                   
        items(items = data.items, { it.id }) { item ->
            CustomReorderableItem(
                reorderableState = state,
                key = item.id,
                modifier = if (data.items.last().id == item.id) {
                    Modifier.padding(bottom = 40.dp)
                } else Modifier
            ) { dragging ->
                val index = data.items.indexOf(item)
                val itemTag = "reorder-item-${item.id}"
                val previousLabel = data.items.getOrNull(index - 1)?.label?.asString()
                val currentLabel = data.items.getOrNull(index)?.label?.asString()
                val nextLabel = data.items.getOrNull(index + 1)?.label?.asString()

                val dragOffset = remember { Animatable(0f) }

                LaunchedEffect(dragging) {
                    if (dragging) {
                        dragOffset.animateTo(10f, animationSpec = tween(200))
                    } else {
                        dragOffset.snapTo(0f)
                    }
                }

                Box(
                    modifier = Modifier
                        .offset(y = dragOffset.value.dp)
                        .fillMaxWidth()
                ) {
                    ListItemDragMlc(
                        data = item,
                        dragging = dragging,
                        state = state,
                        modifier = Modifier
                            .then(
                                if (context.isTalkBackEnabled()) {
                                    Modifier
                                        .clearAndSetSemantics {
                                            contentDescription = currentLabel ?: ""
                                            customActions = listOfNotNull(
                                                previousLabel?.let { label ->
                                                    CustomAccessibilityAction(context.getString(R.string.accessibility_move_up)) {
                                                        val moved = moveItemUp(item, data.items)
                                                        if (moved) {
                                                            accessibilityFocusKey = item.id
                                                            view.announceForAccessibility(
                                                                context.getString(
                                                                    R.string.accessibility_moved_above,
                                                                    label
                                                                )
                                                            )
                                                        }
                                                        true
                                                    }
                                                },
                                                nextLabel?.let { label ->
                                                    CustomAccessibilityAction(context.getString(R.string.accessibility_move_down)) {
                                                        val moved = moveItemDown(item, data.items)
                                                        if (moved) {
                                                            accessibilityFocusKey = item.id
                                                            view.announceForAccessibility(
                                                                context.getString(
                                                                    R.string.accessibility_moved_below,
                                                                    label
                                                                )
                                                            )
                                                        }
                                                        true
                                                    }
                                                }
                                            )
                                        }
                                        .testTag(itemTag)
                                        .focusable()
                                } else Modifier
                            ),
                        onUIAction = onUIAction,
                    )
                }
            }
        }
    }

    if (context.isTalkBackEnabled()) {
        val focusTargetKey = accessibilityFocusKey ?: fallbackFocusKey
        RequestAccessibilityFocusOnVisibleItem(
            itemId = focusTargetKey,
            tagPrefix = "reorder-item-",
            rootView = view,
            listState = state.listState,
            items = data.items,
            onComplete = {
                if (accessibilityFocusKey != null) {
                    accessibilityFocusKey = null
                }
            }
        )
    }
}

fun moveItemUp(
    item: ListItemDragMlcData,
    items: SnapshotStateList<ListItemDragMlcData>
): Boolean {
    val index = items.indexOf(item)
    return if (index > 0) {
        items.swap(index, index - 1)
        true
    } else false
}

fun moveItemDown(
    item: ListItemDragMlcData,
    items: SnapshotStateList<ListItemDragMlcData>
): Boolean {
    val index = items.indexOf(item)
    return if (index >= 0 && index < items.size - 1) {
        items.swap(index, index + 1)
        true
    } else {
        false
    }
}

fun <T> SnapshotStateList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

@SuppressLint("AccessibilityFocus")
@Composable
fun RequestAccessibilityFocusOnVisibleItem(
    itemId: Any?,
    tagPrefix: String,
    rootView: View,
    listState: androidx.compose.foundation.lazy.LazyListState,
    items: List<ListItemDragMlcData>,
    onComplete: () -> Unit
) {
    LaunchedEffect(itemId) {
        val index = items.indexOfFirst { it.id == itemId }
        if (itemId != null && index >= 0) {
            listState.scrollToItem(index)
            delay(300)
            val view = rootView.findViewWithTag<View>("$tagPrefix$itemId")
            view?.let {
                it.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
                it.performAccessibilityAction(
                    AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,
                    null
                )
            }
            onComplete()
        }
    }
}

data class ListItemDragOrgData(
    val items: SnapshotStateList<ListItemDragMlcData>,
    val componentId: UiText? = null
) : UIElementData

// This extension fixes the missing animateItemPlacement in the burnoutcrew library after migrating to androidxComposeBom = "2025.04.01"
@Composable
private fun LazyItemScope.CustomReorderableItem(
    reorderableState: ReorderableState<*>,
    key: Any?,
    modifier: Modifier = Modifier,
    index: Int? = null,
    orientationLocked: Boolean = true,
    content: @Composable BoxScope.(isDragging: Boolean) -> Unit
) = ReorderableItem(
    state = reorderableState,
    key = key,
    modifier = modifier,
    defaultDraggingModifier = Modifier.animateItem(),
    orientationLocked = orientationLocked,
    index = index,
    content = content
)

@Composable
@Preview
fun ListItemDragOrgPreview() {
    val state = ListItemDragOrgData(
        items = SnapshotStateList<ListItemDragMlcData>().apply {
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("Свідоцтво про реєстрацію транспортного засобу"),
                )
            )
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("Закордонний паспорт"),
                    countOfDocGroup = 2
                )
            )
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("ХХ000000"),
                    desc = UiText.DynamicString("Дата видачі: хх.хх.хххх"),
                )
            )
        }
    )
    ListItemDragOrg(
        data = state,
        onMove = { _, _ ->
            /* no-op */
        }
    ) {
        /* no-op */
    }
}