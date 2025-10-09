package ua.gov.diia.ui_base.components.molecule.header

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlcV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconBackArrowAtom
import ua.gov.diia.ui_base.components.atom.icon.MiddleIconAtm
import ua.gov.diia.ui_base.components.atom.icon.MiddleIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun NavigationPanelMlcV2(
    modifier: Modifier = Modifier,
    data: NavigationPanelMlcV2Data,
    lazyListState: LazyListState,
    alphaCallback: (Float) -> Unit = {},
    onUIAction: (UIAction) -> Unit
) {
    val density = LocalDensity.current
    val minThresholdPx = with(density) { 16.dp.toPx() }
    val maxThresholdPx = with(density) { 48.dp.toPx() }

    val isFirstItemVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }

    val scrollOffset by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset.toFloat() }
    }

    val isFirstItemFullyVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                    lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    val targetAlpha = when {
        data.isClosed == true -> 1F
        isFirstItemFullyVisible -> 1F
        isFirstItemVisible -> when {
            scrollOffset < minThresholdPx -> 1F
            scrollOffset in minThresholdPx..maxThresholdPx -> {
                1F - (scrollOffset - minThresholdPx) / (maxThresholdPx - minThresholdPx)
            }

            else -> 0F
        }

        else -> 0F
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 100),
        label = "navPanelAlpha"
    )

    alphaCallback(animatedAlpha)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (data.isClosed == true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                data.iconLeft?.let {
                    MiddleIconAtm(data = it) {
                        onUIAction(UIAction(actionKey = data.backAction))
                    }
                } ?: run {
                    IconBackArrowAtom(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 28.dp)
                            ) {
                                onUIAction(UIAction(actionKey = data.backAction))
                            }
                    )
                }
                data.title?.let {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 32.dp)
                            .weight(1f),
                        text = it.asString(),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = DiiaTextStyle.h4ExtraSmallHeading
                    )
                }
            }
        } else {
            AnimatedVisibility(
                visible = animatedAlpha > 0.5f,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    data.iconLeft?.let {
                        MiddleIconAtm(data = it) {
                            onUIAction(UIAction(actionKey = data.backAction))
                        }
                    } ?: run {
                        IconBackArrowAtom(
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 28.dp)
                                ) {
                                    onUIAction(UIAction(actionKey = data.backAction))
                                }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = animatedAlpha <= 0.5f,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    data.iconLeft?.let {
                        MiddleIconAtm(data = it) {
                            onUIAction(UIAction(actionKey = data.backAction))
                        }
                    } ?: run {
                        IconBackArrowAtom(
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 28.dp)
                                ) {
                                    onUIAction(UIAction(actionKey = data.backAction))
                                }
                        )
                    }
                    data.title?.let {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f),
                            text = it.asString(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = DiiaTextStyle.h4ExtraSmallHeading
                        )
                    }
                }
            }
        }
    }
}

data class NavigationPanelMlcV2Data(
    val backAction: String = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK,
    val title: UiText? = null,
    val componentId: UiText? = null,
    val iconLeft: MiddleIconAtmData? = null,
    val isClosed: Boolean? = false
) : UIElementData


fun NavigationPanelMlcV2.toUiModel(): NavigationPanelMlcV2Data {
    return NavigationPanelMlcV2Data(
        title = this.title.toDynamicStringOrNull(),
        iconLeft = this.iconLeft?.toUiModel(),
        componentId = this.componentId.toDynamicStringOrNull(),
        isClosed = this.isClosed
    )
}

fun generateNavigationPanelMlcV2MockData(): NavigationPanelMlcV2Data {
    return NavigationPanelMlcV2Data(
        title = UiText.DynamicString("Label"),
        iconLeft = MiddleIconAtmData(
            code = DiiaResourceIcon.BACK.code,
        ),
        isClosed = true
    )
}

@Preview
@Composable
fun NavigationPanelMlcV2Preview() {
    val state: LazyListState = rememberLazyListState()
    NavigationPanelMlcV2(
        data = generateNavigationPanelMlcV2MockData(),
        lazyListState = state
    ) {
    }
}