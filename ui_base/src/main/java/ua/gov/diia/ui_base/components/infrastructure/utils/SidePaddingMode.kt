package ua.gov.diia.ui_base.components.infrastructure.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.ElementPaddingMode

enum class SidePaddingMode {
    NONE, MEDIUM, LARGE
}

fun ElementPaddingMode?.toSidePaddingMode(): SidePaddingMode? = when (this) {
    ElementPaddingMode.NONE -> SidePaddingMode.NONE
    ElementPaddingMode.MEDIUM -> SidePaddingMode.MEDIUM
    ElementPaddingMode.LARGE -> SidePaddingMode.LARGE
    else -> null
}

fun SidePaddingMode?.toDp(defaultPadding: Dp): Dp = when (this) {
    SidePaddingMode.NONE -> 0.dp
    SidePaddingMode.MEDIUM -> 16.dp
    SidePaddingMode.LARGE -> 24.dp
    else -> defaultPadding
}