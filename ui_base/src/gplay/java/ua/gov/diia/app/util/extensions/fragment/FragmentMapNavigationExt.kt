package ua.gov.diia.app.util.extensions.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.delegation.WithCrashlytics

fun Fragment.openNavigationMap(lat: Double, lng: Double, title: String, crashlytics: WithCrashlytics) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=$lat,$lng"))
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        fallbackToDefaultNavIntent(lat, lng, title, crashlytics)
    }
}

private fun Fragment.fallbackToDefaultNavIntent(
    lat: Double,
    lng: Double,
    title: String,
    crashlytics: WithCrashlytics,
) {
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