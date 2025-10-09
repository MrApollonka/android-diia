package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.runtime.Composable
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.TridentDefault
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule

@Composable
fun TridentLoaderBlock(
    loader: Loader,
) {
    if (loader.isLoadingFullScreen(indicator = TridentDefault)) {
        TridentLoaderMolecule()
    }
}