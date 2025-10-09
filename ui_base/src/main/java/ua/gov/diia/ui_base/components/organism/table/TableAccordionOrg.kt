package ua.gov.diia.ui_base.components.organism.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.tableAccordionOrg.TableAccordionItem
import ua.gov.diia.core.models.common_compose.table.tableAccordionOrg.TableAccordionOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.BackgroundMode
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.organism.accordion.AccordionOrg
import ua.gov.diia.ui_base.components.organism.accordion.AccordionOrgData
import ua.gov.diia.ui_base.components.organism.accordion.toUIModel
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.Primary

@Composable
fun TableAccordionOrg(
    modifier: Modifier = Modifier,
    data: TableAccordionOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp
            )
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            data.tableMainHeadingMlc?.let {
                TableMainHeadingMlc(
                    data = data.tableMainHeadingMlc,
                    onUIAction = onUIAction
                )
            }

            data.items?.forEachIndexed { index, item ->
                AccordionOrg(
                    modifier = Modifier,
                    data = item,
                    onUIAction = onUIAction
                )
                if ((index + 1) != data.items.size) {
                    DividerSlimAtom(
                        modifier = Modifier
                            .height(1.dp)
                            .padding(
                                horizontal = item.paddingHorizontal.toDp(defaultPadding = 16.dp)),
                        color = BlackSqueeze)
                }
            }

            data.attentionIconMessageMlc?.let {
                AttentionIconMessageMlc(
                    modifier = Modifier,
                    data = data.attentionIconMessageMlc.copy(
                        paddingTop = TopPaddingMode.NONE,
                        paddingHorizontal = SidePaddingMode.NONE
                    ),
                    onUIAction = onUIAction
                )
            }
        }
    }
}


data class TableAccordionOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_ACCORDION_ORG,
    val componentId: UiText? = null,
    val tableMainHeadingMlc: TableMainHeadingMlcData? = null,
    val items: List<AccordionOrgData>? = null,
    val attentionIconMessageMlc: AttentionIconMessageMlcData? = null
) : UIElementData

fun TableAccordionOrg?.toUIModel(): TableAccordionOrgData {
    val entity = this
    val taItems = mutableListOf<AccordionOrgData>().apply {
        if (!entity?.items.isNullOrEmpty()) {
            (entity?.items as List<TableAccordionItem>).forEach { item ->
                add(item.accordionOrg.toUIModel())
            }
        }
    }

    return TableAccordionOrgData(
        componentId = UiText.DynamicString(entity?.componentId.orEmpty()),
        tableMainHeadingMlc = entity?.tableMainHeadingMlc?.toUIModel(),
        items = taItems,
        attentionIconMessageMlc = entity?.attentionIconMessageMlc?.toUiModel()
    )
}

fun generateTableAccordionOrgData(): TableAccordionOrgData {
    val itemsAccordion = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Item title 1"),
            value = "Value_01"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Item title 2"),
            value = "Value_02"
        ),
        TableItemHorizontalMlcData(
            id = "3",
            title = UiText.DynamicString("Item title 3"),
            secondaryTitle = UiText.DynamicString("Description 03"),
            value = "Value_03"
        ),
        TableItemHorizontalMlcData(
            id = "4",
            title = UiText.DynamicString("Item title 4"),
            secondaryTitle = UiText.DynamicString("Description 04"),
            value = "Value_04"
        ),
        TableItemHorizontalMlcData(
            id = "5",
            title = UiText.DynamicString("Item title 5"),
            secondaryTitle = UiText.DynamicString("Description 05"),
            value = "Value_05"
        ),
    )
    val items = listOf(
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Heading_1",
            description = "Description",
            state = true,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion
        ),
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Heading_2",
            description = "Description",
            state = false,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion
        ),
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Heading_3",
            description = "Description",
            state = false,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion
        )
    )
    return TableAccordionOrgData(
        componentId = "ta_01".toDynamicString(),
        tableMainHeadingMlc = TableMainHeadingMlcData(
            paddingTop = TopPaddingMode.LARGE,
            paddingHorizontal = SidePaddingMode.NONE,
            title = "Heading".toDynamicString(),
        ),
        items = items,
        attentionIconMessageMlc = AttentionIconMessageMlcData(
            paddingTop = TopPaddingMode.MEDIUM,
            icon = SmallIconAtmData(code = DiiaResourceIcon.ELLIPSE_INFO.code),
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            backgroundMode = BackgroundMode.NOTE

        )
    )
}

@Composable
@Preview
fun TableAccordionOrgPreview() {
    TableAccordionOrg(
        modifier = Modifier,
        data = generateTableAccordionOrgData(),
        onUIAction = {}
    )
}

@Composable
@Preview
fun TableAccordionOrgRealPreview() {
    val itemsAccordion = listOf(
        TableItemVerticalMlcData(
            id = "1",
            value = UiText.DynamicString("Наявна соціальна допомога:"),
        ),
        TableItemVerticalMlcData(
            id = "2",
            supportText = "•",
            value = UiText.DynamicString("Державної соціальної допомоги особам, які не мають права на пенсію, та особам з інвалідністю.")
        ),
        TableItemVerticalMlcData(
            id = "3",
            supportText = "•",
            value = UiText.DynamicString("Допомога на дітей одиноким матерям")
        ),
    )
    val itemsAccordion2 = listOf(
        TableItemVerticalMlcData(
            id = "1",
            title = UiText.DynamicString("Наявна соціальна допомога:"),
        ),
        TableItemVerticalMlcData(
            id = "2",
            value = UiText.DynamicString("* Державної соціальної допомоги особам, які не мають права на пенсію, та особам з інвалідністю.")
        ),
        TableItemVerticalMlcData(
            id = "3",
            value = UiText.DynamicString("* Допомога на дітей одиноким матерям")
        ),
        TableItemVerticalMlcData(
            id = "1",
            title = UiText.DynamicString("Друга соціальна допомога:"),
        ),
        TableItemVerticalMlcData(
            id = "2",
            value = UiText.DynamicString("* Державної соціальної допомоги особам, які не мають права на пенсію, та особам з інвалідністю.")
        ),
        TableItemVerticalMlcData(
            id = "3",
            value = UiText.DynamicString("* Допомога на дітей одиноким матерям")
        ),

    )
    val itemsAccordion3 = listOf(
        TableItemHorizontalMlcData(
            id = "01",
            title = UiText.DynamicString("Item title 01"),
            secondaryTitle = UiText.DynamicString("Description 01"),
            value = "Value_01"
        ),
        TableItemHorizontalMlcData(
            id = "02",
            title = UiText.DynamicString("Item title 02"),
            secondaryTitle = UiText.DynamicString("Description 02"),
            value = "Value_02"
        ),
        TableItemHorizontalMlcData(
            id = "03",
            title = UiText.DynamicString("Item title 03"),
            secondaryTitle = UiText.DynamicString("Description 03"),
            value = "Value_03"
        ),
        TableItemHorizontalMlcData(
            id = "4",
            title = UiText.DynamicString("Item title 4"),
            secondaryTitle = UiText.DynamicString("Description 04"),
            value = "Value_04"
        ),
        TableItemHorizontalMlcData(
            id = "5",
            title = UiText.DynamicString("Item title 5"),
            secondaryTitle = UiText.DynamicString("Description 05"),
            value = "Value_05"
        ),
    )
    val items = listOf(
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Дія Володимир Святославович",
            description = "02.11.1960 р",
            state = true,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion
        ),
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Heading_2",
            description = "Description",
            state = false,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion2
        ),
        AccordionOrgData(
            paddingTop = TopPaddingMode.NONE,
            paddingHorizontal = SidePaddingMode.NONE,
            heading = "Heading_3",
            description = "Description",
            state = false,
            expandedIcon = SmallIconAtmData(
                componentId = "e_01".toDynamicString(),
                code = "chevronDown",
                accessibilityDescription = "chevronDown",
                action = null
            ),
            collapsedIcon = SmallIconAtmData(
                componentId = "c_02".toDynamicString(),
                code = "chevronUp",
                accessibilityDescription = "chevronUp",
                action = null
            ),
            expandedContent = itemsAccordion3
        )
    )

    val tableAccordion = TableAccordionOrgData(
        componentId = "ta_01".toDynamicString(),
        tableMainHeadingMlc = TableMainHeadingMlcData(
            title = "Heading".toDynamicString(),
//            iconAtmData = IconAtmData(
//                code = "copy"
//            )
        ),
        items = items,
        attentionIconMessageMlc = AttentionIconMessageMlcData(
            paddingTop = TopPaddingMode.MEDIUM,
            icon = SmallIconAtmData(code = DiiaResourceIcon.ELLIPSE_INFO.code),
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            backgroundMode = BackgroundMode.NOTE

        )
    )
    Box(modifier = Modifier.background(Primary)) {
        TableAccordionOrg(
            modifier = Modifier,
            data = tableAccordion,
            onUIAction = {}
        )
    }
}