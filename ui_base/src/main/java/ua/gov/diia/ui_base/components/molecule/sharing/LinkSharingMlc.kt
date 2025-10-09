package ua.gov.diia.ui_base.components.molecule.sharing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.sharing.LinkSharingMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun LinkSharingMlc(
    modifier: Modifier = Modifier,
    data: LinkSharingMlcData
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .height(24.dp)
            .testTag(data.componentId)

    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = data.label,
            color = Black,
            style = DiiaTextStyle.h3SmallHeading,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class LinkSharingMlcData(
    val componentId: String = "linkSharingMlc",
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val label: String
) : UIElementData


fun LinkSharingMlc.toUIModel(): LinkSharingMlcData {
    return LinkSharingMlcData(
        componentId = componentId,
        label = label,
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode()
    )
}

enum class LinkSharingMlcMockTypes {
    NO_PADDINGS_LONG_TEX, PADDINGS, NO_PADDING, PADDINGS_LONG_TEXT
}

fun generateLinkSharingMlcMockData(type: LinkSharingMlcMockTypes): LinkSharingMlcData {
    return when (type) {
        LinkSharingMlcMockTypes.PADDINGS -> LinkSharingMlcData(
            label = "diia.gov.ua/diplink",
            paddingHorizontal = SidePaddingMode.MEDIUM,
            paddingTop = TopPaddingMode.LARGE
        )

        LinkSharingMlcMockTypes.NO_PADDING -> LinkSharingMlcData(label = "diia.gov.ua/diplink")
        LinkSharingMlcMockTypes.PADDINGS_LONG_TEXT -> LinkSharingMlcData(
            label = "diia.com/label/label/label.labellabel/label",
            paddingHorizontal = SidePaddingMode.MEDIUM,
            paddingTop = TopPaddingMode.LARGE
        )

        LinkSharingMlcMockTypes.NO_PADDINGS_LONG_TEX -> LinkSharingMlcData(label = "diia.com/label/label/label.labellabel/label")
    }
}

@Composable
@Preview
fun LinkSharingMlc_Preview_no_paddings() {
    LinkSharingMlc(data = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.NO_PADDING))
}

@Composable
@Preview
fun LinkSharingMlc_Preview_paddings() {
    LinkSharingMlc(
        data = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.PADDINGS)
    )
}

@Composable
@Preview
fun LinkSharingMlc_Preview_no_paddings_long_text() {
    LinkSharingMlc(data = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.NO_PADDINGS_LONG_TEX))
}


@Composable
@Preview
fun LinkSharingMlc_Preview_paddings_long_text() {
    LinkSharingMlc(data = generateLinkSharingMlcMockData(LinkSharingMlcMockTypes.PADDINGS_LONG_TEXT))
}