package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconUrlAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun SmallIconUrlAtm(
    modifier: Modifier = Modifier,
    data: SmallIconUrlAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    AsyncImage(
        modifier = modifier
            .size(24.dp)
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.componentId,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId.orEmpty()),
        model = data.url,
        contentDescription = data.accessibilityDescription,
        contentScale = ContentScale.Fit,
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        error = painterResource(R.drawable.ic_icon_placeholder)
    )
}

data class SmallIconUrlAtmData(
    val actionKey: String = UIActionKeysCompose.SMALL_ICON_URL_ATM,
    val componentId: String?,
    val url: String,
    val accessibilityDescription: String?,
    val action: DataActionWrapper?
)

fun SmallIconUrlAtm.toUIModel() = SmallIconUrlAtmData(
    componentId = componentId,
    url = url,
    accessibilityDescription = accessibilityDescription,
    action = action?.toDataActionWrapper()
)

@Preview
@Composable
fun SmallIconUrlAtmPreview() {
    SmallIconUrlAtm(
        data = SmallIconUrlAtmData(
            componentId = null,
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "SmallIconUrlAtm",
            action = null
        )
    )
}