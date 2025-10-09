package ua.gov.diia.ui_base.components.molecule.button

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.lerp
import ua.gov.diia.core.models.common_compose.mlc.button.BtnSlideMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.util.toDataActionWrapper
import ua.gov.diia.ui_base.util.toUiModel


@Composable
fun BtnSlideMlc(
    data: BtnSlideMlcData,
    onUIAction: (UIAction) -> Unit
) {

    val sliderButtonWidthDp = 48.dp
    val density = LocalDensity.current
    val sliderButtonWidthPx = with(density) { sliderButtonWidthDp.toPx() }
    var boxWidthPx by remember { mutableIntStateOf(0) }

    val sliderPosition = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    /**
     * Tracks whether the user is currently interacting with the slider,
     * either by dragging or by pressing the arrow icon.
     */
    var isInteracting by remember { mutableStateOf(false) }

    val dragProgress = remember(sliderPosition.value, boxWidthPx) {
        if (boxWidthPx > 0) {
            (sliderPosition.value / (boxWidthPx - sliderButtonWidthPx)).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    /**
     * Determine text alpha based on user interaction and drag progress:
     * While interacting and drag < 89%, text alpha is semi-transparent (0.54).
     * When drag reaches 89% or more while interacting, text fades out completely (0).
     * Otherwise, text is fully visible (1).
     */
    val targetAlpha = when {
        isInteracting && dragProgress < 0.89f -> 0.54f
        isInteracting && dragProgress >= 0.89f -> 0f
        else -> 1f
    }

    val effectiveTextAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(64.dp)
            .testTag(data.componentId?.asString() ?: "")
            .onSizeChanged { size ->
                boxWidthPx = size.width
            }
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = White,
                    shape = RoundedCornerShape(32.dp)
                )
        ) {
            Text(
                text = data.label,
                style = DiiaTextStyle.t2TextDescription,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(effectiveTextAlpha)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newPosition = sliderPosition.value + delta
                        val maxPosition = boxWidthPx - sliderButtonWidthPx
                        scope.launch {
                            sliderPosition.snapTo(newPosition.coerceIn(0f, maxPosition))
                        }
                    },
                    onDragStarted = {
                        isInteracting = true
                    },
                    onDragStopped = {
                        isInteracting = false
                        val maxPosition = boxWidthPx - sliderButtonWidthPx
                        val target = if (sliderPosition.value < maxPosition * 0.89f) {
                            0f // Go back
                        } else {
                            maxPosition // Go to end
                        }
                        scope.launch {
                            sliderPosition.animateTo(
                                target,
                                animationSpec = tween(durationMillis = 300)
                            )
                            if (target == maxPosition) {
                                onUIAction.invoke(UIAction(data.actionKey, action = data.action))
                            }
                        }
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val boxWidthDp = with(density) { boxWidthPx.toDp() }
            SliderButton(
                icon = data.icon,
                progress = dragProgress,
                maxWidth = boxWidthDp,
                onSliderBtnPressed = { interacting ->
                    isInteracting = interacting
                },
            )
        }
        
        if (data.state == UIState.Interaction.Disabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .clickable(enabled = true, onClick = {})
            )
        }

    }
}

/**
 *  @param progress 0f to 1f, where 0f = collapsed, 1f = fully expanded
 */
@Composable
private fun SliderButton(
    icon: IconAtmData? = null,
    progress: Float,
    maxWidth: Dp,
    onSliderBtnPressed: (Boolean) -> Unit = {}
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedWidth = lerp(48.dp, maxWidth, clampedProgress)

    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .height(48.dp)
            .width(animatedWidth)
            .background(
                color = Black,
                shape = RoundedCornerShape(32.dp)
            ),
        contentAlignment = Alignment.CenterEnd
    ) {

        Image(
            painter = painterResource(
                id = DiiaResourceIcon.getResourceId(icon?.code ?: DiiaResourceIcon.ARROW_RIGHT.code)
            ),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.CenterEnd)
                .size(24.dp)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            onSliderBtnPressed(true)
                            true
                        }

                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            onSliderBtnPressed(false)
                            true
                        }

                        else -> false
                    }
                })
    }
}

data class BtnSlideMlcData(
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.BTN_SLIDE_MLC,
    val label: String,
    val icon: IconAtmData? = null,
    val state: UIState.Interaction? = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null,
) : UIElementData

fun BtnSlideMlc.toUiModel(): BtnSlideMlcData {
    return BtnSlideMlcData(
        componentId = UiText.DynamicString(componentId.orEmpty()),
        label = label,
        icon = icon?.toUiModel(),
        state = when (state) {
            "disabled" -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        },
        action = action?.toDataActionWrapper(),


        )
}

enum class BtnSlideMlcMockType {
    ENABLED_START,
    DISABLED_START
}

fun getBtnSlideMlcPreviewData(mockType: BtnSlideMlcMockType): BtnSlideMlcData {
    return when (mockType) {
        BtnSlideMlcMockType.ENABLED_START -> BtnSlideMlcData(label = "Потягніть вправо, як надішлете")
        BtnSlideMlcMockType.DISABLED_START -> BtnSlideMlcData(
            label = "Потягніть вправо, як надішлете",
            state = UIState.Interaction.Disabled
        )
    }
}

@Composable
@Preview
fun BtnSlideMlc_Preview() {
    BtnSlideMlc(getBtnSlideMlcPreviewData(BtnSlideMlcMockType.ENABLED_START)) { }
}

@Composable
@Preview
fun BtnSlideMlc_Preview_Disabled() {
    BtnSlideMlc(getBtnSlideMlcPreviewData(BtnSlideMlcMockType.DISABLED_START)) { }
}

@Composable
@Preview
fun SliderButtonPreview_Start() {
    SliderButton(null, 0f, 300.dp)
}

@Composable
@Preview
fun SliderButtonPreview_ThirtyPerc() {
    SliderButton(null, 0.3f, 300.dp)
}

@Composable
@Preview
fun SliderButtonPreview_End() {
    SliderButton(null, 1f, 300.dp)
}