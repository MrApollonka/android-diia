package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ua.gov.diia.core.models.common_compose.atm.icon.IconUrlAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun IconUrlAtm(
    modifier: Modifier = Modifier,
    data: IconUrlAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    AsyncImage(
        modifier = modifier
            .size(32.dp)
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .semantics {
                testTag = data.componentId
            },
        model = data.url,
        contentDescription = data.accessibilityDescription,
        contentScale = ContentScale.Fit,
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        error = painterResource(R.drawable.ic_icon_placeholder)
    )
}

data class IconUrlAtmData(
    val actionKey: String = UIActionKeysCompose.ICON_URL_ATM,
    val id: String? = null,
    val componentId: String = "",
    val url: String?,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null,
)

fun IconUrlAtm.toUiModel(id: String? = null, actionKey: String? = null): IconUrlAtmData {
    return IconUrlAtmData(
        actionKey = actionKey ?: UIActionKeysCompose.ICON_URL_ATM,
        id = id,
        componentId = componentId.orEmpty(),
        url = url,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

@Preview
@Composable
fun IconUrlAtmPreview() {
    val data = IconUrlAtmData(
        id = "1",
        url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
        accessibilityDescription = "Button"
    )
    IconUrlAtm(data = data)
}