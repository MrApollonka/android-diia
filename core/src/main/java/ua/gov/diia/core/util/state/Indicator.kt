package ua.gov.diia.core.util.state


sealed class Indicator(open val key: String)

//PAGE_LOADING_TRIDENT
data object TridentDefault : Indicator("pageLoadingTrident")

//PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
data object TridentWithBackNav : Indicator("pageLoadingTridentWithBackNavigation")

//PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
data object TridentUiBlocking : Indicator("pageLoadingTridentWithUIBlocking")

// PAGE_LOADING_CIRCULAR
data object Circular : Indicator("pageLoadingCircular")

//""
data object Empty : Indicator("")