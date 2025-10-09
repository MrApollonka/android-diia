package ua.gov.diia.app.util.extensions.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.delegation.WithCrashlytics

fun Fragment.openNavigationMap(lat: Double, lng: Double, title: String, crashlytics: WithCrashlytics) {
    //TODO replace with huawei specific maps
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:<$lat>,<$lng>?q=<$lat>,<$lng>(${title})")
    )
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        crashlytics.sendNonFatalError(e)
    }
}