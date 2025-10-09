package ua.gov.diia.ui_base.fragments

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ua.gov.diia.core.util.extensions.context.serviceClipboard
import ua.gov.diia.ui_base.util.view.showTopSnackBar


fun Fragment.openAppSettings() {
    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", requireContext().packageName, null)
    }
    startActivity(intent)
}

fun Fragment.copyToClipboard(
    label: String,
    content: String,
    @StringRes messageResId: Int,
    topPadding: Boolean = false
) {
    val clip = ClipData.newPlainText(label, content)
    (context ?: return).serviceClipboard?.setPrimaryClip(clip)
    view?.showTopSnackBar(messageResId, Snackbar.LENGTH_LONG, topPadding)
}




