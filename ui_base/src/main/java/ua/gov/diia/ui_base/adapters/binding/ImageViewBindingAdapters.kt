package ua.gov.diia.ui_base.adapters.binding

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import coil.dispose
import coil.load
import ua.gov.diia.core.util.extensions.context.getColorCompatSafe
import ua.gov.diia.core.util.extensions.context.getDrawableSafe

fun ImageView.srcFromUri(uri: String?) {
    if (uri == null) return

    load(uri.toUri())
}

fun ImageView.srcFromUrl(url: String?) {
    if (url == null) return

    load(url)
}

fun ImageView.srcFromRes(@DrawableRes res: Int) {
    setImageDrawable(context.getDrawableSafe(res))
}

fun ImageView.tintFromRes(@ColorRes res: Int?) {
    val colorCompatRes = context.getColorCompatSafe(res) ?: return
    setColorFilter(colorCompatRes, android.graphics.PorterDuff.Mode.SRC_IN)
}

fun ImageView.rotateRes(rotate: Boolean?) {
    rotation = if (rotate == true) 180f else 0f
}

fun ImageView.srcUrlWithPlaceholder(url: String?, placeholder: Int?) {
    if (url.isNullOrEmpty()) {
        if (placeholder == null) {
            dispose()
        } else {
            load(placeholder) {
                crossfade(true)
            }
        }
    } else {
        load(url) {
            placeholder?.let { lPlaceholder -> error(lPlaceholder) }
            crossfade(true)
        }
    }
}