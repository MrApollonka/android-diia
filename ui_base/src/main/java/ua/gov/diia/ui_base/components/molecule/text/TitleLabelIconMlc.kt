package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.TitleLabelIconMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.LogoAtm
import ua.gov.diia.ui_base.components.atom.icon.LogoAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TitleLabelIconMlc(
    modifier: Modifier = Modifier,
    data: TitleLabelIconMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(data.componentId.orEmpty())
    ) {
        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (data.logoAtm != null) {
                LogoAtm(
                    data = data.logoAtm,
                    onUIAction = onUIAction
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = data.label,
                color = Black,
                style = DiiaTextStyle.h4ExtraSmallHeading
            )
        }
    }
}

data class TitleLabelIconMlcData(
    val componentId: String? = null,
    val label: String,
    val logoAtm: LogoAtmData? = null
) : UIElementData

fun TitleLabelIconMlc.toUiModel() = TitleLabelIconMlcData(
    componentId = componentId,
    label = label,
    logoAtm = logoAtm?.toUiModel()
)

enum class TitleLabelIconMlcType {
    IconWithLabel, ErrorIconWithLabel, OnlyLabel
}

fun generateTitleLabelIconMlcData(mockType: TitleLabelIconMlcType): TitleLabelIconMlcData {

    return when (mockType) {
        TitleLabelIconMlcType.IconWithLabel -> TitleLabelIconMlcData(
            label = "Label with icon",
            logoAtm = LogoAtmData(
                logo = "https://go.diia.app/assets/img/pages/screen-phone.png",
                placeholder = DiiaResourceIcon.DEFAULT_ICON_LARGE.code
            )
        )

        TitleLabelIconMlcType.ErrorIconWithLabel -> TitleLabelIconMlcData(
            label = "Label with error icon",
            logoAtm = LogoAtmData(
                logo = "",
                placeholder = DiiaResourceIcon.DEFAULT_ICON_LARGE.code
            )
        )

        TitleLabelIconMlcType.OnlyLabel -> TitleLabelIconMlcData(
            label = "Label without icon"
        )
    }
}

@Preview
@Composable
fun LogoAtmPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TitleLabelIconMlc(
            data = generateTitleLabelIconMlcData(TitleLabelIconMlcType.IconWithLabel),
            onUIAction = {
                /* no-op */
            }
        )
        TitleLabelIconMlc(
            data = generateTitleLabelIconMlcData(TitleLabelIconMlcType.ErrorIconWithLabel),
            onUIAction = {
                /* no-op */
            }
        )
        TitleLabelIconMlc(
            data = generateTitleLabelIconMlcData(TitleLabelIconMlcType.OnlyLabel),
            onUIAction = {
                /* no-op */
            }
        )
    }
}