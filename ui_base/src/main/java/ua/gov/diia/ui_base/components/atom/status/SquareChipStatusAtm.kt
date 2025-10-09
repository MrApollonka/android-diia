package ua.gov.diia.ui_base.components.atom.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.chip.SquareChipStatusAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.Grey
import ua.gov.diia.ui_base.components.theme.SteelBlue

@Composable
fun SquareChipStatusAtm(
    modifier: Modifier = Modifier,
    data: SquareChipStatusAtmData
) {
    Box(
        modifier = modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 24.dp),
                top = data.paddingTop.toDp(defaultPadding = 16.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 24.dp)
            )
            .height(18.dp)
            .background(
                color = when (data.type) {
                    SquareChipType.GRAY -> Grey
                    SquareChipType.BLUE -> SteelBlue
                }, shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 1.dp)
            .testTag(data.componentId?.asString() ?: ""),
        contentAlignment = Alignment.Center
    ) {
        data.title?.let {
            Text(
                textAlign = TextAlign.Center,
                text = it,
                color = Black, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }

}

data class SquareChipStatusAtmData(
    val componentId: UiText? = null,
    val type: SquareChipType,
    val title: String? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

enum class SquareChipType {
    BLUE, GRAY;
}

fun SquareChipStatusAtm.toUiModel(): SquareChipStatusAtmData {
    return SquareChipStatusAtmData(
        title = name,
        type = when (type) {
            SquareChipStatusAtm.ChipType.BLUE -> SquareChipType.BLUE
            SquareChipStatusAtm.ChipType.GRAY -> SquareChipType.GRAY
            else -> SquareChipType.GRAY
        },
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        componentId = this.componentId.toDynamicStringOrNull()
    )
}

fun generateChipStatusAtmMockData(type: SquareChipType): SquareChipStatusAtmData {
    return SquareChipStatusAtmData(
        componentId = null,
        type = type,
        title = "Status",
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE
    )
}

@Preview
@Composable
fun SquareChipStatusAtm_Blue() {
    val data = generateChipStatusAtmMockData(type = SquareChipType.GRAY)
    SquareChipStatusAtm(
        modifier = Modifier, data = data
    )
}

@Preview
@Composable
fun SquareChipStatusAtm_Gray() {
    val state = generateChipStatusAtmMockData(type = SquareChipType.BLUE)
    SquareChipStatusAtm(
        modifier = Modifier, data = state
    )
}