package ua.gov.diia.scanner.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.frame.Frame
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnIconCircledWhiteAtm
import ua.gov.diia.ui_base.components.atom.button.BtnIconCircledWhiteAtmData
import ua.gov.diia.ui_base.components.atom.icon.BarcodeScannerCardAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.mappers.loader.mapToLoader
import ua.gov.diia.ui_base.models.orientation.Orientation

@Composable
fun BarcodeScannerScreen(
    modifier: Modifier = Modifier,
    data: BarcodeScannerScreenData,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    onUIAction: (UIAction) -> Unit,
    processFrame: (Frame, CameraView, Rect) -> Unit,
    orientation: Orientation
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
                if (data.closeCameraView) {
                    if (it.isOpened) it.close()
                } else {
                    if (!it.isOpened) it.open()
                }
                it.flash = if (data.isFlashOn) Flash.TORCH else Flash.OFF
            },
            modifier = modifier
                .fillMaxSize()
                .constrainAs(createRef()) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .testTag(stringResource(R.string.mrz_scanner_body_test_tag))
        )
        when (data.scannerType) {
            ScannerType.Square -> {
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
            }

            ScannerType.Rectangle -> {
                // TODO: Add new BarcodeScannerRectangleCardAtom?
            }
        }
        NavigationPanelMlc(
            modifier = Modifier
                .statusBarsPadding()
                .constrainAs(navPanelRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            data = data.navigationPanelMlcData,
            onUIAction = onUIAction
        )
        Text(
            modifier = Modifier
                .padding(24.dp)
                .constrainAs(createRef()) {
                    start.linkTo(navPanelRef.start)
                    end.linkTo(navPanelRef.end)
                    top.linkTo(navPanelRef.bottom)
                },
            text = data.description.asString(),
            color = Color.White,
            style = DiiaTextStyle.t3TextBody,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .constrainAs(createRef()) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            BtnIconCircledWhiteAtm(
                modifier = Modifier.semantics {
                    contentDescription =
                        context.getString(R.string.accessibility_turn_on_the_flashlight)
                    role = Role.Button
                },
                data = BtnIconCircledWhiteAtmData(
                    actionKey = UIActionKeysCompose.BTN_FLASH_ON_OFF,
                    icon = if (data.isFlashOn) UiText.StringResource(R.drawable.ic_flash_scan_white) else UiText.StringResource(
                        R.drawable.ic_flash_scan
                    ),
                    interactionState = UIState.Interaction.Enabled
                ),
                onUIAction = {
                    onUIAction(
                        UIAction(
                            actionKey = UIActionKeysCompose.BTN_FLASH_ON_OFF,
                            data = data.isFlashOn.not().toString(),
                        )
                    )
                }
            )
            data.btnKeyboardScan?.let { lBtnKeyboardScan ->
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                BtnIconCircledWhiteAtm(
                    modifier = Modifier
                        .semantics {
                            contentDescription =
                                context.getString(R.string.accessibility_enter_barcode_manually)
                            role = Role.Button
                        },
                    data = lBtnKeyboardScan,
                    onUIAction = onUIAction
                )
            }
        }
        TridentLoaderWithUIBlocking(
            loader = mapToLoader(content = contentLoaded)
        )
    }
}

data class BarcodeScannerScreenData(
    val navigationPanelMlcData: NavigationPanelMlcData,
    val description: UiText,
    val btnKeyboardScan: BtnIconCircledWhiteAtmData? = null,
    val closeCameraView: Boolean = false,
    val isFlashOn: Boolean = false,
    val scannerType: ScannerType = ScannerType.Square
) : UIElementData

enum class ScannerType {
    Square, Rectangle
}

@Preview
@Composable
private fun BarcodeScannerScreenPreview() {
    val barcodeScannerScreenData = BarcodeScannerScreenData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = "Сканування".toDynamicString(),
            isContextMenuExist = false,
            tintColor = White,
        ),
        description = "Опис".toDynamicString()
    )
    BarcodeScannerScreen(
        data = barcodeScannerScreenData,
        onUIAction = {
            /* no-op */
        },
        processFrame = { _, _, _ ->
            /* no-op */
        },
        orientation = Orientation.Portrait
    )
}