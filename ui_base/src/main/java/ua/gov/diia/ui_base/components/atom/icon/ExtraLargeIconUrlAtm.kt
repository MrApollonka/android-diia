package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.EXTRA_LARGE_ICON_URL_ATM
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderSpinnerLoaderAtm

@Composable
fun ExtraLargeIconUrlAtm(
    modifier: Modifier = Modifier,
    data: ExtraLargeIconUrlAtmData,
    onUIAction: (UIAction) -> Unit
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (data.action == null && context.isTalkBackEnabled()) {
                    Modifier.focusable()
                } else {
                    Modifier.noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = data.componentId,
                                action = data.action
                            )
                        )
                    }
                }
            )

            .testTag(data.componentId.orEmpty()),
        model = data.iconUrl,
        contentDescription = data.accessibilityDescription
            ?: context.getString(R.string.accessibility_participant_photo),
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(DiiaResourceIcon.getResourceId(DiiaResourceIcon.PLACEHOLDER.code)),
                    contentDescription = null
                )
            }
        }
    )
}

data class ExtraLargeIconUrlAtmData(
    val actionKey: String = EXTRA_LARGE_ICON_URL_ATM,
    val componentId: String? = null,
    val iconUrl: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null
) : UIElementData

fun generateMockExtraLargeIconUrlAtmData() = ExtraLargeIconUrlAtmData(
    iconUrl = "https://go.diia.app/assets/img/pages/screen-phone.png"
)

@Preview
@Composable
private fun ExtraLargeIconUrlAtmPreview() {
    ExtraLargeIconUrlAtm(
        data = generateMockExtraLargeIconUrlAtmData(),
        onUIAction = {
            /* no-op */
        }
    )
}