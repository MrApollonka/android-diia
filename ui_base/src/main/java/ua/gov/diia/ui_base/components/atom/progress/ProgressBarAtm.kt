package ua.gov.diia.ui_base.components.atom.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.theme.AzureRadiance
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ProgressBarAtm(
    modifier: Modifier = Modifier,
    data: ProgressBarAtmData
) {
    Row(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = modifier
                .weight(1F)
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(BlackSqueeze)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = data.percent)
                    .fillMaxHeight()
                    .background(AzureRadiance)
            )
        }
        data.percentText?.let { lPercentText ->
            Text(
                modifier = Modifier
                    .width(56.dp),
                style = DiiaTextStyle.t3TextBody,
                text = lPercentText,
                color = Black,
                textAlign = TextAlign.End
            )
        }
    }
}

data class ProgressBarAtmData(
    val componentId: String? = null,
    val percent: Float,
    val percentText: String?,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData

fun generateMockProgressBarAtm() = ProgressBarAtmData(
    percent = 0.1F,
    percentText = "10.00%"
)

@Preview
@Composable
private fun ProgressBarAtmPreview() {
    ProgressBarAtm(
        data = generateMockProgressBarAtm()
    )
}