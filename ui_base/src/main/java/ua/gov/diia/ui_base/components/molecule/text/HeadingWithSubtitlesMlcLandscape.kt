package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun HeadingWithSubtitlesMlcLandscape(
    modifier: Modifier = Modifier,
    data: HeadingWithSubtitlesMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = data.componentId }
    ) {
        data.value?.let {
            // Manual trim is needed as BE send us data with new line break symbols
            val text = it.replace(Regex("\\r\\n|\\r|\\n"), " ")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Start)
            ) {
                AutoSizeLimitedText(
                    text = text,
                    modifier = Modifier,
                    maxLines = 2,
                    style = DiiaTextStyle.h3SmallHeading,
                )
            }
        }

        if (!data.subtitles.isNullOrEmpty()) {
            data.subtitles.forEach {
                val topPadding = if (data.value == null && data.subtitles.indexOf(it) == 0) 8.dp else 16.dp
                Text(
                    modifier = Modifier.padding(top = topPadding),
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HeadingWithSubtitlesMlcLandscapePreview() {

    val subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
    val data = HeadingWithSubtitlesMlcData(
        value = "Паспорт громадянина",
        subtitles = subtitles
    )

    HeadingWithSubtitlesMlcLandscape(data = data)
}

@Composable
@Preview(showBackground = true)
fun HeadingWithSubtitlesMlcLandscapeEmptyValuePreview() {

    val subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
    val data = HeadingWithSubtitlesMlcData(
        value = null,
        subtitles = subtitles
    )

    HeadingWithSubtitlesMlcLandscape(data = data)
}