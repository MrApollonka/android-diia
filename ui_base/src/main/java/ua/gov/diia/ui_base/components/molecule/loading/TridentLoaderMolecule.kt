package ua.gov.diia.ui_base.components.molecule.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm

@Composable
fun TridentLoaderMolecule(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val tridentLoaderText = context.getString(R.string.accessibility_data_is_loading_please_wait)

    LaunchedEffect(key1 = Unit) {
        view.announceForAccessibility(tridentLoaderText)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = tridentLoaderText
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            TridentLoaderAtm(
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun DocsTabLoadingMoleculePreview() {
    TridentLoaderMolecule()
}