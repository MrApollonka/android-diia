package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import ua.gov.diia.core.util.state.Circular
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.getLegacyContentLoaded
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenTransparentMolecule
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderBlock
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithNavigationBlock
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking

@Composable
fun ComposeRootScreen(
    modifier: Modifier = Modifier,
    loader: Loader = Loader.create(),
    toolbar: @Composable (() -> Unit)? = null,
    body: @Composable (ColumnScope.() -> Unit)? = null,
    centeredBody: @Composable (ColumnScope.() -> Unit)? = null,
    bottom: @Composable (() -> Unit)? = null,
    onEvent: (UIAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column {
                toolbar?.let {
                    toolbar()
                }
                body?.let {
                    body()
                }
                centeredBody?.let {
                    centeredBody()
                }
                bottom?.let {
                    bottom()
                }
            }
            if (loader.isLoadingByComponent()) {
                FullScreenTransparentMolecule()
            }
        }
        if (loader.isLoadingFullScreen(Circular)) {
            FullScreenLoadingMolecule()
        }
        TridentLoaderBlock(loader = loader)
        TridentLoaderWithNavigationBlock(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentLoaded = loader.getLegacyContentLoaded(),
            onUIAction = onEvent
        )
        TridentLoaderWithUIBlocking(
            loader = loader
        )
    }
}