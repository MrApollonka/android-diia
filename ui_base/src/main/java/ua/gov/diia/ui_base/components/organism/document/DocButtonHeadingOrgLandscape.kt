package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcLandscape
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcLandscape
import ua.gov.diia.ui_base.components.molecule.doc.StackMlc
import ua.gov.diia.ui_base.components.molecule.doc.StackMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcLandscape
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcLandscape

@Composable
fun DocButtonHeadingOrgLandscape(
    modifier: Modifier,
    data: DocButtonHeadingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics {
                testTag = data.componentId
            },
        verticalAlignment = Alignment.Bottom
    ) {
        data.heading?.let {
            HeadingWithSubtitlesMlcLandscape(
                modifier = Modifier.weight(1f),
                data = it
            )
        }
        data.headingWhite?.let {
            HeadingWithSubtitlesWhiteMlcLandscape(
                modifier = Modifier.weight(1f),
                data = it
            )
        }

        data.docNumberCopy?.let {
            DocNumberCopyMlcLandscape(
                modifier = Modifier.weight(1f),
                data = it,
                onUIAction = onUIAction
            )
        }

        data.docNumberCopyWhite?.let {
            DocNumberCopyWhiteMlcLandscape(
                modifier = Modifier.weight(1f),
                data = it,
                onUIAction = onUIAction
            )
        }

        data.stackMlcData?.let {
            if (data.isStack && data.size != 1) {
                StackMlc(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_STACK)) }
                )
            }
        }

        data.iconAtmData?.let {
            if (!data.isStack) {
                IconAtm(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU)) }
                )
            }
        }
    }
}


@Composable
@Preview
fun DocButtonHeadingOrgLandscapePreview() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
        isStack = false
    )
    DocButtonHeadingOrgLandscape(modifier = Modifier, data = data) {}
}

@Composable
@Preview
fun DocButtonHeadingOrgLandscapePreviewDocCopy() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        docNumberCopy = DocNumberCopyMlcData(
            id = "123",
            value = "1234567890".toDynamicString(),
            icon = IconAtmData(
                code = DiiaResourceIcon.COPY.code
            )
        ),
        isStack = false,
        iconAtmData = IconAtmData(code = DiiaResourceIcon.ELLIPSE_KEBAB.code),
    )
    DocButtonHeadingOrgLandscape(modifier = Modifier, data = data) {}
}

@Composable
@Preview
fun DocButtonHeadingOrgLandscapeWithStackPreview() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
        isStack = true,
        size = 2,
        stackMlcData = StackMlcData(
            amount = 3, smallIconAtmData = SmallIconAtmData(
                id = "1",
                code = DiiaResourceIcon.STACK.code,
                accessibilityDescription = "Button"
            )
        ),
    )
    DocButtonHeadingOrgLandscape(modifier = Modifier, data = data) {}
}