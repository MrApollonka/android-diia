package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.PHOTO_CARD_MLC
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlcV2Data
import ua.gov.diia.ui_base.components.molecule.checkbox.generateMockCheckboxMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm
import ua.gov.diia.ui_base.components.theme.AzureMist
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@Composable
fun PhotoCardMlc(
    modifier: Modifier = Modifier,
    data: PhotoCardMlcData,
    isFocused: Boolean,
    onUIAction: (UIAction) -> Unit
) {
    val imagePainter = rememberAsyncImagePainter(data.imageUrl)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8F)
            .clip(RoundedCornerShape(16.dp))
            .testTag(data.componentId.orEmpty())
    ) {
        AnimatedContent(
            targetState = isFocused,
            transitionSpec = {
                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
            }
        ) { focused ->
            if (focused) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.65F)
                            .background(AzureMist)
                            .noRippleClickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = data.componentId,
                                        action = data.action
                                    )
                                )
                            }
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = imagePainter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        if (imagePainter.state is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(56.dp),
                                    painter = painterResource(
                                        DiiaResourceIcon.getResourceId(
                                            DiiaResourceIcon.PLACEHOLDER.code
                                        )
                                    ),
                                    contentDescription = null
                                )
                            }
                        }
                        data.iconRight?.let { lIconRight ->
                            IconAtm(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.TopEnd),
                                data = lIconRight,
                                onUIAction = onUIAction
                            )
                        }
                    }
                    data.checkboxMlcData?.let { lCheckboxMlcData ->
                        CheckboxMlc(
                            modifier = Modifier
                                .weight(1F)
                                .background(White)
                                .noRippleClickable {
                                    onUIAction(lCheckboxMlcData.action())
                                }
                                .padding(16.dp),
                            data = lCheckboxMlcData,
                            onUIAction = onUIAction
                        )
                    }
                }
                if (imagePainter.state is AsyncImagePainter.State.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        TridentLoaderAtm()
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WhiteAlpha50)
                )
            }
        }
    }
}

data class PhotoCardMlcData(
    val actionKey: String = PHOTO_CARD_MLC,
    val componentId: String? = null,
    val imageUrl: String,
    val iconRight: IconAtmData? = null,
    val checkboxMlcData: CheckboxMlcData? = null,
    val radioBtnMlcV2Data: RadioBtnMlcV2Data? = null,
    val action: DataActionWrapper? = null
) : UIElementData {

    fun onCheckClicked(): PhotoCardMlcData {
        return this.copy(
            checkboxMlcData = checkboxMlcData?.onCheckboxClick()
        )
    }

    fun updateInteractionState(newState: UIState.Interaction): PhotoCardMlcData {
        return this.copy(
            checkboxMlcData = checkboxMlcData?.updateInteractionState(
                newState = newState
            )
        )
    }

}

fun generateMockPhotoCardMlcData() = PhotoCardMlcData(
    imageUrl = "https://go.diia.app/assets/img/pages/screen-phone.png",
    iconRight = IconAtmData(
        code = DiiaResourceIcon.INFO_BLACK_ROUND.code
    ),
    checkboxMlcData = generateMockCheckboxMlcData()
)

@Preview
@Composable
private fun PhotoCardMlcPreview() {
    PhotoCardMlc(
        data = generateMockPhotoCardMlcData(),
        isFocused = true,
        onUIAction = {
            /* no-op */
        }
    )
}