package ua.gov.diia.scanner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.frame.Frame
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtmData
import ua.gov.diia.ui_base.components.atom.icon.BarcodeScannerCardAtom
import ua.gov.diia.ui_base.components.atom.icon.IconBackArrowAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.models.orientation.Orientation

@Composable
fun QrcodeScannerScreen(
    modifier: Modifier = Modifier,
    data: QrcodeScannerScreenData?,
    onUIAction: (UIAction) -> Unit,
    processFrame: (Frame, CameraView, Rect) -> Unit,
    orientation: Orientation,
) {
    val context = LocalContext.current
    val sizeScanBar = remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }
    val lifecycleOwner = LocalLifecycleOwner.current

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .provideTestTagsAsResourceId()
    ) {
        val (navPanelRef) = createRefs()

        AndroidView(
            modifier = modifier
                .fillMaxSize()
                .constrainAs(createRef()) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .testTag(stringResource(R.string.mrz_scanner_body_test_tag)),
            factory = {
                CameraView(context).apply {
                    setLifecycleOwner(lifecycleOwner)
                    audio = Audio.OFF
                    useDeviceOrientation = false
                    addFrameProcessor { frame ->
                        processFrame.invoke(frame, this, sizeScanBar.value)
                    }
                }
            },
            update = {
                if (data != null) {
                    if (data.closeCameraView) {
                        if (it.isOpened) it.close()
                    } else {
                        if (!it.isOpened) it.open()
                    }
                }
            }
        )
        BarcodeScannerCardAtom(
            modifier = modifier
                .fillMaxSize()
                .constrainAs(createRef()) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            framePosition = {
                sizeScanBar.value = it
            },
            orientation = orientation
        )

        data?.title?.let {
            Box(
                modifier = modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .constrainAs(navPanelRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(
                        start = 24.dp,
                        top = if (orientation == Orientation.Landscape) 12.dp else 32.dp,
                        end = 24.dp,
                        bottom = 16.dp
                    )
            ) {
                if (data.btnLogout == null) {
                    IconBackArrowAtom(
                        modifier = Modifier
                            .size(28.dp)
                            .align(alignment = Alignment.CenterStart)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 28.dp)
                            ) {
                                onUIAction(UIAction(actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
                            },
                        tintColor = Color.White
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(alignment = Alignment.Center),
                    text = it.asString(),
                    style = DiiaTextStyle.h3SmallHeading,
                    color = Color.White
                )
            }
        }
        data?.description?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = 34.dp)
                    .constrainAs(createRef()) {
                        start.linkTo(navPanelRef.start)
                        end.linkTo(navPanelRef.end)
                        top.linkTo(navPanelRef.bottom)
                    },
                text = it.asString(),
                color = Color.White,
                style = DiiaTextStyle.t2TextDescription,
                textAlign = TextAlign.Center
            )
        }
        data?.btnLogout?.let {
            BtnStrokeWhiteAtm(
                modifier = Modifier
                    .constrainAs(createRef()) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .navigationBarsPadding()
                    .padding(bottom = 40.dp),
                data = it,
                onUIAction = onUIAction
            )
        }
    }
}

data class QrcodeScannerScreenData(
    val title: UiText? = null,
    val description: UiText,
    val closeCameraView: Boolean = false,
    val btnLogout: BtnStrokeWhiteAtmData? = null
) : UIElementData