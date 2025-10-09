package ua.gov.diia.ui_base.components.organism.qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.qr.QrCodeOrg
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlc
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlcData
import ua.gov.diia.ui_base.components.molecule.code.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.PaginationMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.PaginationMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.generatePaginationMessageMlcMockData
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.subatomic.timer.ExpireLabel
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun QrCodeOrg(
    modifier: Modifier = Modifier,
    data: QrCodeOrgData,
    onUIAction: (UIAction) -> Unit
) {
    var expired by remember { mutableStateOf(false) }

    var measuredHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val measuredHeightDp = with(density) { measuredHeightPx.toDp() }

    Box(
        modifier = modifier
            .testTag(tag = data.componentId)
            .height(332.dp)
    ) {
        if (expired && data.paginationMessageMlc != null) {
            val heightModifier = if (measuredHeightPx > 0) {
                Modifier.height(measuredHeightDp)
            } else {
                Modifier.wrapContentHeight()
            }
            PaginationMessageMlc(
                modifier = heightModifier,
                data = data.paginationMessageMlc,
                onUIAction = onUIAction
            )
        } else {
            Column(
                modifier = modifier
                    .wrapContentWidth()
                    .onGloballyPositioned { coords ->
                        measuredHeightPx = coords.size.height
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                data.text?.let {
                    Text(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        text = data.text, style = DiiaTextStyle.t2TextDescription,
                        textAlign = TextAlign.Center
                    )
                } ?: Spacer(modifier = Modifier.height(16.dp))

                data.qrCode?.let {
                    QrCodeMlc(
                        modifier = Modifier.padding(
                            start = 32.dp,
                            end = 32.dp,
                        ), data = it
                    )
                }
                if (data.timer != null && data.expireLabelFirst != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ExpireLabel(
                        expireLabelFirst = data.expireLabelFirst,
                        timer = data.timer,
                        expireLabelLast = data.expireLabelLast,
                        expired = expired
                    ) {
                        expired = true
                    }
                }
            }
        }
    }
}

data class QrCodeOrgData(
    val componentId: String,
    val text: String? = null,
    val qrCode: QrCodeMlcData? = null,
    val expireLabelFirst: UiText? = null,
    val expireLabelLast: UiText? = null,
    val timer: Int? = null,
    val paginationMessageMlc: PaginationMessageMlcData? = null
) : UIElementData

fun QrCodeOrg.toUIModel(): QrCodeOrgData {
    return QrCodeOrgData(
        componentId = this.componentId,
        text = text,
        qrCode = qrCodeMlc.toUIModel(),
        expireLabelFirst = expireLabel?.expireLabelFirst.toDynamicStringOrNull(),
        expireLabelLast = expireLabel?.expireLabelLast.toDynamicStringOrNull(),
        timer = expireLabel?.timer,
        paginationMessageMlc = stateAfterExpiration?.paginationMessageMlc?.toUIModel()
    )
}

@Preview
@Composable
fun QrCodeOrg_Preview() {
    val data = QrCodeOrgData(
        text = "Text",
        componentId = "qrCodeOrg",
        qrCode = QrCodeMlcData(
            qrLink = UiText.DynamicString("www.google.com")
        ),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 10,
        paginationMessageMlc = generatePaginationMessageMlcMockData()
    )
    QrCodeOrg(data = data) {}
}


@Preview
@Composable
fun QrCodeOrg_NoTime_noText_Preview() {
    val data = QrCodeOrgData(
        text = null,
        componentId = "qrCodeOrg",
        qrCode = QrCodeMlcData(
            qrLink = UiText.DynamicString("www.google.com")
        )
    )
    QrCodeOrg(data = data) {}
}