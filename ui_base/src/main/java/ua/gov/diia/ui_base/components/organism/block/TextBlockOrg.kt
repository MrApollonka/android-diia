package ua.gov.diia.ui_base.components.organism.block

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.block.text.Item
import ua.gov.diia.core.models.common_compose.org.block.text.ListItem
import ua.gov.diia.core.models.common_compose.org.block.text.TextBlockOrg
import ua.gov.diia.core.models.common_compose.org.block.text.TextItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.org.block.text.TextItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.status.SquareChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.SquareChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.SquareChipType
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.list.block.text.TextBlockItem
import ua.gov.diia.ui_base.components.molecule.list.block.text.TextItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.block.text.TextItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.block.text.TextItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.block.text.TextItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.block.text.toUiModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUiModel
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TextBlockOrg(
    modifier: Modifier = Modifier,
    data: TextBlockOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.chipSquareChipStatusAtm?.let {
            SquareChipStatusAtm(data = it)
            Spacer(Modifier.size(16.dp))
        }
        data.title?.let {
            Text(
                text = it.asString(),
                modifier = Modifier.padding(top = if(data.chipSquareChipStatusAtm == null) 0.dp else 16.dp),
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = Black
            )
            Spacer(Modifier.size(16.dp))
        }
        data.text?.let {
            Text(
                text = it.asString(),
                modifier = Modifier,
                style = DiiaTextStyle.t1BigText,
                color = Black
            )
        }
        if (!data.items.isNullOrEmpty() || !data.listItems.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        data.listItems?.forEachIndexed { index, item ->
            when (item) {
                is TableItemVerticalMlcData -> {
                    TableItemVerticalMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TableItemHorizontalMlcData -> {
                    TableItemHorizontalMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                else -> {
                    //nothing
                }
            }
            if (index != data.listItems.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        data.items?.forEachIndexed { index, item ->
            when (item) {
                is TextItemHorizontalMlcData -> {
                    TextItemHorizontalMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TextItemVerticalMlcData -> {
                    TextItemVerticalMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                else -> {
                    //nothing
                }
            }
            if (index != data.items.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

data class TextBlockOrgData(
    val actionKey: String = UIActionKeysCompose.TEXT_BLOCK_ORG,
    val componentId: UiText? = null,
    val items: List<TextBlockItem>? = null,
    val listItems: List<TableBlockItem>? = null,
    val chipSquareChipStatusAtm: SquareChipStatusAtmData? = null,
    val text: UiText?,
    val title: UiText?,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

fun TextBlockOrg?.toUIModel(): TextBlockOrgData {
    val entity = this
    val tbItems = mutableListOf<TextBlockItem>()
    if (!entity?.items.isNullOrEmpty()) {
        tbItems.apply {
            (entity?.items as List<Item>).forEach { item ->
                if (item.textItemHorizontalMlc is TextItemHorizontalMlc) {
                    add(
                        (item.textItemHorizontalMlc as TextItemHorizontalMlc).toUiModel()
                    )
                }
                if (item.textItemVerticalMlc is TextItemVerticalMlc) {
                    add(
                        (item.textItemVerticalMlc as TextItemVerticalMlc).toUiModel()
                    )
                }
            }
        }
    }

    val tbListItems = mutableListOf<TableBlockItem>()
    if (!entity?.listItems.isNullOrEmpty()) {
        tbListItems.apply {
            (entity?.listItems as List<ListItem>).forEach { item ->
                if (item.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                    add(
                        (item.tableItemHorizontalMlc as TableItemHorizontalMlc).toUiModel()
                    )
                }
                if (item.tableItemVerticalMlc is TableItemVerticalMlc) {
                    add(
                        (item.tableItemVerticalMlc as TableItemVerticalMlc).toUiModel()
                    )
                }
            }
        }
    }

    return TextBlockOrgData(
        chipSquareChipStatusAtm = this?.squareChipStatusAtm?.toUiModel(),
        items = tbItems,
        listItems = tbListItems,
        title = this?.title?.let { UiText.DynamicString(it) },
        text = this?.text?.let { UiText.DynamicString(it) },
        paddingTop = this?.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this?.paddingMode?.side.toSidePaddingMode(),
    )
}

fun generateTextBlockOrgMockData(): TextBlockOrgData {
    val items = listOf(
        TextItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("label_1"),
            value = UiText.DynamicString("value_1"),
            iconRight = SmallIconAtmData(
                id = "1",
                code = DiiaResourceIcon.PLACEHOLDER.code,
                accessibilityDescription = "Button"
            )
        ),
        TextItemVerticalMlcData(
            id = "123",
            title = UiText.DynamicString("label_2"),
            value = UiText.DynamicString("value_2"),
            iconRight = SmallIconAtmData(
                id = "2",
                code = DiiaResourceIcon.PLACEHOLDER.code,
                accessibilityDescription = "Button"
            )
        ),
        TextItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("label_3"),
            value = UiText.DynamicString("value_3"),
            iconRight = SmallIconAtmData(
                id = "3",
                code = DiiaResourceIcon.PLACEHOLDER.code,
                accessibilityDescription = "Button"
            )
        ),
        TextItemVerticalMlcData(
            id = "123",
            title = UiText.DynamicString("label_4"),
            value = UiText.DynamicString("value_4"),
            iconRight = SmallIconAtmData(
                id = "4",
                code = DiiaResourceIcon.PLACEHOLDER.code,
                accessibilityDescription = "Button"
            )
        ),
    )
    return TextBlockOrgData(
        items = items,
        chipSquareChipStatusAtm = SquareChipStatusAtmData(
            componentId = null,
            type = SquareChipType.BLUE,
            title = "Status",
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
        ),
        text = UiText.DynamicString("text"),
        title = UiText.DynamicString("title")
    )
}

@Preview
@Composable
fun TextBlockOrgPreview() {
    val data = generateTextBlockOrgMockData()
    TextBlockOrg(
        modifier = Modifier,
        data = data
    ) {
    }
}

@Preview
@Composable
fun TextBlockOrgWithTableItemPreview() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Title"),
            secondaryTitle = UiText.DynamicString("Secondary title"),
            supportText = "С.1.3",
            value = "Label Value",
            secondaryValue = "Secondary label",
            iconRight = UiText.StringResource(R.drawable.ic_copy)
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Title"),
            secondaryTitle = UiText.DynamicString("Secondary title"),
            supportText = "С.1.3",
            value = "Label Value",
            secondaryValue = "Secondary label",
            iconRight = UiText.StringResource(R.drawable.ic_copy)
        )
    )
    TextBlockOrg(
        modifier = Modifier,
        data = TextBlockOrgData(
            listItems = items,
            chipSquareChipStatusAtm = SquareChipStatusAtmData(
                componentId = null,
                type = SquareChipType.BLUE,
                title = "Status",
                paddingTop = TopPaddingMode.NONE,
                paddingHorizontal = SidePaddingMode.NONE,
            ),
            text = UiText.DynamicString("text"),
            title = UiText.DynamicString("title")
        )
    ) {
    }
}

@Preview
@Composable
fun TextBlockOrgWithTableListItemPreview() {
    val listItems = listOf(
        TableItemVerticalMlcData(
            componentId = "123",
            pointSupportingValue = "1.",
            title = UiText.DynamicString("Допомога на дітей одиноким матерям.")

        ),
        TableItemVerticalMlcData(
            componentId = "123",
            pointSupportingValue = "2.",
            title = UiText.DynamicString("Державної соціальної допомоги особам, які не мають права на пенсію, та особам з інвалідністю.")

        )
    )
    TextBlockOrg(
        modifier = Modifier,
        data = TextBlockOrgData(
            listItems = listItems,
            chipSquareChipStatusAtm = SquareChipStatusAtmData(
                componentId = null,
                type = SquareChipType.BLUE,
                title = "Status",
                paddingTop = TopPaddingMode.NONE,
                paddingHorizontal = SidePaddingMode.NONE,
            ),
            title = UiText.DynamicString("Title"),
            text = UiText.DynamicString("Some text"),
        )
    ) {
    }
}

@Preview
@Composable
fun TextBlockOrgWithTableItemPreview_No_status() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Title"),
            secondaryTitle = UiText.DynamicString("Secondary title"),
            supportText = "С.1.3",
            value = "Label Value",
            secondaryValue = "Secondary label",
            iconRight = UiText.StringResource(R.drawable.ic_copy)
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Title"),
            secondaryTitle = UiText.DynamicString("Secondary title"),
            supportText = "С.1.3",
            value = "Label Value",
            secondaryValue = "Secondary label",
            iconRight = UiText.StringResource(R.drawable.ic_copy)
        )
    )
    TextBlockOrg(
        modifier = Modifier,
        data = TextBlockOrgData(
            listItems = items,
            chipSquareChipStatusAtm = null,
            text = UiText.DynamicString("text"),
            title = UiText.DynamicString("title")
        )
    ) {
    }
}