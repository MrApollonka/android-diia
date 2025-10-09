package ua.gov.diia.ui_base.components.atom.space

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.SpacerAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

@Composable
fun SpacerAtm(
    data: SpacerAtmData
) {
    Spacer(
        modifier = Modifier
            .height(
                when (data.type) {
                    SpacerAtmType.SMALL -> 8.dp
                    SpacerAtmType.MEDIUM -> 16.dp
                    SpacerAtmType.LARGE -> 24.dp
                    SpacerAtmType.EXTRA_LARGE -> 32.dp
                }
            )
            .testTag(data.componentId?.asString() ?: "")
    )
}

data class SpacerAtmData(
    val type: SpacerAtmType,
    val componentId: UiText? = null
) : UIElementData

enum class SpacerAtmType {
    SMALL, MEDIUM, LARGE, EXTRA_LARGE
}

fun SpacerAtm?.toUiModel(): SpacerAtmData? {
    if (this == null) return null
    val type = when (this.type) {
        "small" -> SpacerAtmType.SMALL
        "medium" -> SpacerAtmType.MEDIUM
        "large" -> SpacerAtmType.LARGE
        "extra_large" -> SpacerAtmType.EXTRA_LARGE
        else -> return null
    }
    val componentId = this.componentId?.let { UiText.DynamicString(it) }
    return SpacerAtmData(type = type, componentId = componentId)
}