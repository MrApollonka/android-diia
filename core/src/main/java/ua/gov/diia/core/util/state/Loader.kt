package ua.gov.diia.core.util.state

/**
 * Holds loader information for specific screen.
 * When screen state is loading, matching key elements will display progress or matching trident will be shown.
 */


sealed class Loader(open val isLoading: Boolean) {

    fun setComponent(key: String): Component = Component(key = key, isLoading = isLoading)

    fun setFullScreen(indicator: Indicator): FullScreen =
        FullScreen(indicator = indicator, isLoading = isLoading)

    /**
     * Check if loading by key in progress
     * @param key will check loading by specific key. If empty, no loading in progress
     */
    fun isLoadingByComponent(key: String = ""): Boolean {
        return this is Component && this.isLoading && key.isNotEmpty() && this.key == key
    }

    /**
     * Check if loading content in progress
     * @param indicator will check loading content with specific trident.
     * If null, just loading by Loader.State content is checked
     */
    fun isLoadingFullScreen(indicator: Indicator? = null): Boolean {
        return this is FullScreen && indicator != null && this.isLoading && this.indicator == indicator
    }

    fun stop(): Loader = when (this) {
        is Component -> copy(isLoading = false)
        is FullScreen -> copy(isLoading = false)
        is None -> None
    }

    fun start(): Loader = when (this) {
        is Component -> copy(isLoading = true)
        is FullScreen -> copy(isLoading = true)
        is None -> None
    }

    companion object {
        fun createComponent(key: String = "", isLoading: Boolean = false): Loader {
            return Component(key = key, isLoading = isLoading)
        }

        fun createFullScreen(indicator: Indicator = Empty, isLoading: Boolean = false): Loader {
            return FullScreen(indicator = indicator, isLoading = isLoading)
        }

        fun create(): Loader = None
    }
}

data class Component(val key: String = "", override val isLoading: Boolean) : Loader(isLoading)

data class FullScreen(val indicator: Indicator, override val isLoading: Boolean) : Loader(isLoading)

data object None : Loader(isLoading = false)