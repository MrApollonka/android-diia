package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import ua.gov.diia.core.models.common_compose.atm.icon.LogoAtm
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderSpinnerLoaderAtm
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun LogoAtm(
    modifier: Modifier = Modifier,
    data: LogoAtmData,
    onUIAction: (UIAction) -> Unit
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .size(64.dp)
            .clip(RoundedCornerShape(20.dp))
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
        model = data.logo,
        contentDescription = data.accessibilityDescription,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoaderSpinnerLoaderAtm()
            }
        },
        error = {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(DiiaResourceIcon.getResourceId(data.placeholder)),
                contentDescription = null
            )
        }
    )
}

data class LogoAtmData(
    val actionKey: String = UIActionKeysCompose.LOGO_ATM,
    val componentId: String? = null,
    val logo: String,
    val placeholder: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null
) : UIElementData

fun LogoAtm.toUiModel() = LogoAtmData(
    componentId = componentId,
    logo = logo,
    placeholder = placeholder ?: DiiaResourceIcon.DEFAULT_ICON_LARGE.code,
    accessibilityDescription = accessibilityDescription,
    action = action?.toDataActionWrapper()
)

@Preview
@Composable
fun LogoAtmPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoAtm(
            data = LogoAtmData(
                logo = "https://go.diia.app/assets/img/pages/screen-phone.png",
                placeholder = DiiaResourceIcon.DEFAULT_ICON_LARGE.code
            ),
            onUIAction = {
                /* no-op */
            }
        )
        LogoAtm(
            data = LogoAtmData(
                logo = "",
                placeholder = DiiaResourceIcon.DEFAULT_ICON_LARGE.code
            ),
            onUIAction = {
                /* no-op */
            }
        )
    }
}