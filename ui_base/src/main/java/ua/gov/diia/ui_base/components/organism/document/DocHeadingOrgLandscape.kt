package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcLandscape
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcLandscape
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcLandscape
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcLandscape

@Composable
fun DocHeadingOrgLandscape(
    modifier: Modifier = Modifier,
    data: DocHeadingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = data.componentId }
    ) {
        Column(modifier = Modifier.weight(1f)) {
            data.heading?.let { HeadingWithSubtitlesMlcLandscape(data = it) }
            data.headingWhite?.let { HeadingWithSubtitlesWhiteMlcLandscape(data = it) }
            data.docNumber?.let { DocNumberCopyMlcLandscape(data = it, onUIAction = onUIAction) }
            data.docNumberCopyWhite?.let {
                DocNumberCopyWhiteMlcLandscape(
                    data = it,
                    onUIAction = onUIAction
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun DocHeadingOrgLandscapePreview() {
    DocHeadingOrgLandscape(data = generateDocHeadingOrgMockData()) {}
}