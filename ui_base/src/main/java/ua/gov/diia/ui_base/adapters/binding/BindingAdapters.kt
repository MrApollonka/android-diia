package ua.gov.diia.ui_base.adapters.binding

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import ua.gov.diia.core.util.extensions.context.getDrawableSafe

fun ImageView.bindBase64(imgBase64: String?) {
    try {
        imgBase64?.let {
            val imageBytes = Base64.decode(it, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            setImageBitmap(decodedImage)
        }
    } catch (e: Exception) {
    }
}

fun setImageDrawableCompat(view: ImageView, @DrawableRes imageResource: Int) {
    view.setImageResource(imageResource)
}

fun TextView.setImageDrawableEndCompat(imageResource: Int) {
    val drawable = context.getDrawableSafe(imageResource)
    val existingDrawables = compoundDrawablesRelative
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        existingDrawables[0],
        existingDrawables[1],
        drawable,
        existingDrawables[3]
    )
}

fun TextView.setImageDrawableStartCompat(imageResource: Int) {
    val drawable = context.getDrawableSafe(imageResource)
    val existingDrawables = compoundDrawablesRelative
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawable,
        existingDrawables[1],
        existingDrawables[2],
        existingDrawables[3]
    )
}

fun TextView.setImageDrawableStartCompat(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun View.setHeightMatchParent(isMatchParent: Boolean) {
    val params = layoutParams
    params.height = if (isMatchParent) {
        ViewGroup.LayoutParams.MATCH_PARENT
    } else {
        ViewGroup.LayoutParams.WRAP_CONTENT
    }
    layoutParams = params
}

fun View.setBackgroundResourceCompat(@DrawableRes drawable: Int) {
    background = ContextCompat.getDrawable(context, drawable)
}