package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.getLegacyContentLoaded
import ua.gov.diia.core.util.state.getLegacyProgress
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootCenteredContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootCollapseContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootContainer
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.mappers.loader.mapToLoader

/**
 * @param loader Used to manage loading state. **Use this instead of `contentLoaded` and `progressIndicator`.**
 * @param contentLoaded (Deprecated) Use [loader] instead.
 * @param progressIndicator (Deprecated) Use [loader] instead.
 */
@Composable
fun PublicServiceScreen(
    modifier: Modifier = Modifier,
    loader: Loader? = null,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>? = null,
    centeredBody: SnapshotStateList<UIElementData>? = null,
    bottom: SnapshotStateList<UIElementData>,
    useGradientBg: Boolean = false,
    collapsingToolBar: Boolean = false,
    onEvent: (UIAction) -> Unit
) {

    val collapsingLazyListState = rememberLazyListState()


    BackHandler {
        onEvent(UIAction(actionKey = toolbar.firstOrNull {
            it is NavigationPanelMlcData
        }?.let {
            (it as NavigationPanelMlcData).backAction
        } ?: UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
    }
    ComposeRootScreen(
        modifier = modifier
            .conditional(!useGradientBg) {
                background(BlackSqueeze)
            }
            .conditional(useGradientBg) {
                paint(
                    painterResource(id = R.drawable.bg_blue_yellow_gradient),
                    contentScale = ContentScale.FillBounds
                )
            }
            .provideTestTagsAsResourceId(),
        loader = loader ?: mapToLoader(
            progress = progressIndicator,
            content = contentLoaded
        ),
        toolbar = {
            if (collapsingToolBar) {
                ToolbarRootCollapseContainer(
                    toolbar = toolbar,
                    lazyListState = collapsingLazyListState,
                    onEvent = onEvent
                )
            } else {
                ToolbarRootContainer(
                    toolbarViews = toolbar,
                    contentLoaded = loader?.getLegacyContentLoaded() ?: contentLoaded,
                    onUIAction = onEvent
                )
            }
        },
        body = {
            body?.let {
                BodyRootLazyContainer(
                    bodyViews = it,
                    displayBlockDivider = bottom.isNotEmpty(),
                    progressIndicator = loader?.getLegacyProgress() ?: progressIndicator,
                    contentLoaded = loader?.getLegacyContentLoaded() ?: contentLoaded,
                    useGradientBg = useGradientBg,
                    lazyListState = if (collapsingToolBar) collapsingLazyListState else rememberLazyListState(),
                    onUIAction = onEvent
                )
            }
        },
        centeredBody = {
            if (centeredBody != null) {
                BodyRootCenteredContainer(
                    centeredBodyViews = centeredBody,
                    loader = loader ?: mapToLoader(
                        progress = progressIndicator,
                        content = contentLoaded
                    ),
                    onUIAction = onEvent
                )
            }
        },
        bottom = {
            BottomBarRootContainer(
                bottomViews = bottom,
                loader = loader ?: mapToLoader(
                    progress = progressIndicator,
                    content = contentLoaded
                ),
                onUIAction = onEvent
            )
        },
        onEvent = onEvent
    )
}