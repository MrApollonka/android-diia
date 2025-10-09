package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun HeadingWithSubtitlesWhiteMlcLandscape(
    modifier: Modifier = Modifier,
    data: HeadingWithSubtitlesWhiteMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        data.value?.let {
            val text = it.replace(Regex("\\r\\n|\\r|\\n"), " ")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Start)
            ) {
                AutoSizeLimitedText(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    color = White,
                    style = DiiaTextStyle.h3SmallHeading,
                )
            }
        }

        if (!data.subtitles.isNullOrEmpty()) {
            data.subtitles.forEach {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = White
                )
            }
        }
    }
}

@Composable
@Preview
fun HeadingWithSubtitlesWhiteMlcLandscapePreview() {
    val subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
    val data = HeadingWithSubtitlesWhiteMlcData(
        value = "Паспорт громадянинаПаспорт громадянинаПаспорт громадянинаПаспорт громадянинаПаспорт громадянина",
        subtitles = subtitles
    )

    HeadingWithSubtitlesWhiteMlcLandscape(data = data)
}