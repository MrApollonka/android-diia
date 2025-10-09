package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.payment.PaymentInfoOrgV2
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalLargeMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalLargeMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.model.TableItemLabelAlignment
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUiModel
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun PaymentInfoOrgV2(
    modifier: Modifier = Modifier,
    data: PaymentInfoOrgV2Data
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.title?.let {
            Text(
                text = it.asString(),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = Black,
                textAlign = TextAlign.Center
            )
        }
        data.subtitle?.let {
            Text(
                text = it.asString(),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                style = DiiaTextStyle.t3TextBody,
                color = BlackAlpha50,
                textAlign = TextAlign.Center
            )
        }
        if (!data.itemsTableBlock.isNullOrEmpty()) {
            data.itemsTableBlock.forEach {
                if (it is TableItemHorizontalMlcData) {
                    TableItemHorizontalMlc(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        data = it.copy(valueAlignment = TableItemLabelAlignment.End)
                    )
                }
            }
        }

        data.tableItemHorizontalLargeMlc?.let {
            DividerSlimAtom(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = BlackSqueeze
            )
            TableItemHorizontalLargeMlc(
                modifier = Modifier.padding(16.dp),
                data = it.copy(valueAlignment = TableItemLabelAlignment.End)
            )
        }
    }
}

data class PaymentInfoOrgV2Data(
    val itemsTableBlock: List<TableBlockItem>? = null,
    val componentId: UiText? = null,
    val title: UiText?,
    val subtitle: UiText?,
    val tableItemHorizontalLargeMlc: TableItemHorizontalLargeMlcData?
) : UIElementData

fun PaymentInfoOrgV2.toUiModel(): PaymentInfoOrgV2Data {
    val itemsTableBlock = mutableListOf<TableBlockItem>()
    this.items?.forEach {
        it.tableItemHorizontalMlc?.toUiModel()?.let { itemsTableBlock.add(it) }
    }
    return PaymentInfoOrgV2Data(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        title = this.title?.let { UiText.DynamicString(it) },
        subtitle = this.subtitle?.let { UiText.DynamicString(it) },
        tableItemHorizontalLargeMlc = this.tableItemHorizontalLargeMlc?.toUiModel(),
        itemsTableBlock = itemsTableBlock
    )
}

fun generatePaymentInfoOrgV2DataMockData(): PaymentInfoOrgV2Data {
    val horizontalMlc = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("title"),
        value = "1 000.00 грн"
    )
    val horizontalLargeMlc = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("title"),
        value = "1 000.00 грн"
    )
    return PaymentInfoOrgV2Data(
        itemsTableBlock = listOf(
            horizontalMlc, horizontalMlc, horizontalMlc
        ),
        tableItemHorizontalLargeMlc = horizontalLargeMlc,
        title = UiText.DynamicString("title"),
        subtitle = UiText.DynamicString("subtitle"),

        )
}

@Composable
@Preview
fun PaymentInfoOrgV2Data_Preview() {
    val data = generatePaymentInfoOrgV2DataMockData()
    PaymentInfoOrgV2(
        data = data
    )
}