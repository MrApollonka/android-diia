package ua.gov.diia.ui_base.util.navigation

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.ContextCompat

object NavigationBarUtils {

    fun setNavigationBarColor(dialog: Dialog, colorRes: Int) {
        val window = dialog.window ?: return
        val metrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = window.context.getSystemService(WindowManager::class.java)
            val windowMetrics = windowManager?.currentWindowMetrics
            val bounds = windowMetrics?.bounds ?: return
            metrics.widthPixels = bounds.width()
            metrics.heightPixels = bounds.height()
        } else {
            window.windowManager.defaultDisplay.getRealMetrics(metrics)
        }

        val color = ContextCompat.getColor(window.context, colorRes)
        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(color)

        val layers = arrayOf<Drawable>(GradientDrawable(), navigationBarDrawable)
        val windowBackground = LayerDrawable(layers)

        windowBackground.setLayerInsetTop(1, metrics.heightPixels)

        window.setBackgroundDrawable(windowBackground)
    }
}
