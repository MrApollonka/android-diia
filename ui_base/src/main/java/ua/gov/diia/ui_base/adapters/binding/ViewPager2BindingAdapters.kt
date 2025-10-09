package ua.gov.diia.ui_base.adapters.binding

import androidx.viewpager2.widget.ViewPager2
import ua.gov.diia.ui_base.util.view.hasNextPage
import ua.gov.diia.ui_base.util.view.hasPreviousPage

fun ViewPager2.setOnPageNavigationListener(
    hasPreviousPageListener: ((Boolean) -> Unit)?,
    hasNextPageListener: ((Boolean) -> Unit)?
) {
    val callback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            if (hasPreviousPageListener != null && hasNextPageListener != null) {
                hasPreviousPageListener(hasPreviousPage)
                hasNextPageListener(hasNextPage)
            }
        }
    }

    registerOnPageChangeCallback(callback)
}