package ua.gov.diia.core.util.extensions.loger

import android.util.Log
import ua.gov.diia.core.util.isDevMode

fun logD(key: String, value: String) {
    if (isDevMode()) {
        Log.d(key, value)
    }
}

fun logV(key: String, value: String) {
    if (isDevMode()) {
        Log.v(key, value)
    }
}

fun logI(key: String, value: String) {
    if (isDevMode()) {
        Log.i(key, value)
    }
}

fun logE(key: String, value: String) {
    if (isDevMode()) {
        Log.e(key, value)
    }
}

fun logW(key: String, value: String ) {
    if (isDevMode()) {
        Log.w(key, value)
    }
}