package ua.gov.diia.ui_base.util.extensions.accessibility

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled

fun View.setAccessibilityRole(roleDescription: String) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.roleDescription = roleDescription
        }
    })
}

fun View.setAccessibilitySelectedState(isSelected: Boolean) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.isSelected = isSelected
        }
    })
}

fun View.setAccessibilityStateDescription(isSelected: Boolean) {
    val selectedText = context.getString(R.string.accessibility_button_state_selected)
    val unselectedText = context.getString(R.string.accessibility_button_state_unselected)
    ViewCompat.setStateDescription(this, if (isSelected) selectedText else unselectedText)
}

fun View.requestAccessibilityFocus() {
    if (context.isTalkBackEnabled()) {
        viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver?.removeOnGlobalLayoutListener(this)
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED)
            }
        })
    }
}

fun ViewPager2.setupAccessibilityFocusSync() {
    val recyclerView = this.getChildAt(0) as? RecyclerView ?: return

    var lastCenteredPosition = RecyclerView.NO_POSITION

    recyclerView.accessibilityDelegate = object : View.AccessibilityDelegate() {
        override fun onRequestSendAccessibilityEvent(
            host: ViewGroup,
            child: View,
            event: AccessibilityEvent
        ): Boolean {
            if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                val position = recyclerView.getChildAdapterPosition(child)
                if (position == RecyclerView.NO_POSITION) {
                    return super.onRequestSendAccessibilityEvent(host, child, event)
                }

                if (position != lastCenteredPosition) {
                    lastCenteredPosition = position

                    this@setupAccessibilityFocusSync.setCurrentItem(position, true)

                    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            recyclerView.smoothScrollToPosition(position)
                            child.requestFocus()
                        }
                    })
                }
            }

            return super.onRequestSendAccessibilityEvent(host, child, event)
        }
    }
}