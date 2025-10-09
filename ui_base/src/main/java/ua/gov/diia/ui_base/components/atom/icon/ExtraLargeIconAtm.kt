package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.icon.ExtraLargeIconAtm
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun ExtraLargeIconAtm(
    modifier: Modifier = Modifier,
    data: ExtraLargeIconAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(56.dp)
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
        painter = painterResource(
            id = DiiaResourceIcon.getResourceId(data.code)
        ),
        contentDescription = data.accessibilityDescription
    )
}

data class ExtraLargeIconAtmData(
    val actionKey: String = UIActionKeysCompose.EXTRA_LARGE_ICON_ATM_DATA,
    val id: String? = null,
    val componentId: UiText? = null,
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null,
)

fun ExtraLargeIconAtm.toUiModel(id: String? = null): ExtraLargeIconAtmData {
    return ExtraLargeIconAtmData(
        componentId = componentId.toDynamicStringOrNull(),
        id = id,
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

@Preview
@Composable
fun ExtraLargeIconAtmPreview() {
    val data = ExtraLargeIconAtmData(
        id = "1",
        code = DiiaResourceIcon.MENU.code,
        accessibilityDescription = "Button"
    )
    ExtraLargeIconAtm(data = data)
}
