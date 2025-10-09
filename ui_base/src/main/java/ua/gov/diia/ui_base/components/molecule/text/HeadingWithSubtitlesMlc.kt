package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun HeadingWithSubtitlesMlc(
    modifier: Modifier = Modifier,
    data: HeadingWithSubtitlesMlcData,
    isFocusable: Boolean = false,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .fillMaxWidth()
            .semantics {
                testTag = data.componentId
            }
    ) {
        data.value?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(alignment = Alignment.Start)
            ) {
                AutoSizeLimitedText(
                    text = data.value,
                    modifier = Modifier
                        .then(
                            if (isFocusable) {
                                Modifier.focusable()
                            } else Modifier
                        ),
                    maxLines = 3
                )
            }
        }

        if (!data.subtitles.isNullOrEmpty()) {
            data.subtitles.forEach {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .focusable(),
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            }
        }
    }
}

data class HeadingWithSubtitlesMlcData(
    val componentId: String = "",
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val value: String?,
    val subtitles: List<String>? = null,
) : UIElementData

fun HeadingWithSubtitlesMlc.toUiModel(): HeadingWithSubtitlesMlcData {
    return HeadingWithSubtitlesMlcData(
        componentId = this.componentId.orEmpty(),
        paddingTop = this.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this.paddingMode?.side.toSidePaddingMode(),
        value = this.value,
        subtitles = if (this.subtitles.isNullOrEmpty()) null else this.subtitles
    )
}

@Composable
@Preview(showBackground = true)
fun HeadingWithSubtitlesMlcPreview() {

    val subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
    val data = HeadingWithSubtitlesMlcData(
        value = "Паспорт громадянина",
        subtitles = subtitles
    )

    HeadingWithSubtitlesMlc(data = data)
}