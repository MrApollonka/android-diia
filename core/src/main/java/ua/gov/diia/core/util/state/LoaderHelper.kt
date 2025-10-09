package ua.gov.diia.core.util.state

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow


fun Loader.getLegacyProgress(): Pair<String, Boolean> {
    return if (this is Component) this.key to this.isLoading
    else "" to false
}

fun Loader.getLegacyContentLoaded(): Pair<String, Boolean> {
    return if (this is FullScreen) this.indicator.getLegacyKey() to !this.isLoading
    else "" to true
}

fun MutableStateFlow<Loader>.start() {
    value = value.start()
}

fun MutableStateFlow<Loader>.stop() {
    value = value.stop()
}

fun MutableStateFlow<Loader>.setComponent(key: String): MutableStateFlow<Loader> {
    value = Component(key = key, isLoading = value.isLoading)
    return this
}

fun MutableStateFlow<Loader>.setFullScreen(indicator: Indicator): MutableStateFlow<Loader> {
    value = FullScreen(indicator = indicator, isLoading = value.isLoading)
    return this
}

fun MutableLiveData<Loader>.start() {
    value = value?.start()
}

fun MutableLiveData<Loader>.stop() {
    value = value?.stop()
}

fun Indicator.getLegacyKey(): String {
    return this.key
}

fun String.toFullScreenIndicator(): Indicator {
    return when (this) {
        "pageLoadingTrident" -> TridentDefault
        "pageLoadingTridentWithUIBlocking" -> TridentUiBlocking
        "pageLoadingTridentWithBackNavigation" -> TridentWithBackNav
        "pageLoadingCircular" -> Circular
        else -> Empty
    }
}