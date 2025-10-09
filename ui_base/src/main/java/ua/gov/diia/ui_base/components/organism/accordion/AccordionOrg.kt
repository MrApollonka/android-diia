package ua.gov.diia.ui_base.components.organism.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.accordion.AccordionOrg
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUiModel
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun AccordionOrg(
    modifier: Modifier = Modifier,
    data: AccordionOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val expandState = remember {
        mutableStateOf(
            if (data.state == true)
                UIState.Expand.Expanded else UIState.Expand.Collapsed
        )
    }

    LaunchedEffect(key1 = expandState.value) {
        onUIAction(
            UIAction(
                actionKey = data.actionKey,
                states = listOf(expandState.value)
            )
        )
    }
    Column(
        modifier
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Column(modifier = Modifier){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .noRippleClickable {
                        expandState.value = when (expandState.value) {
                            UIState.Expand.Collapsed -> UIState.Expand.Expanded
                            UIState.Expand.Expanded -> UIState.Expand.Collapsed
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    data.heading?.let {
                        Text(
                            modifier = Modifier,
                            text = it,
                            color = Black,
                            style = DiiaTextStyle.h4ExtraSmallHeading
                        )
                    }
                    data.description?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = it,
                            color = BlackAlpha50,
                            style = DiiaTextStyle.t2TextDescription
                        )
                    }
                }
                if (expandState.value == UIState.Expand.Expanded) {
                    data.expandedIcon?.let {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp),
                        ) {
                            SmallIconAtm(
                                data = it,
                                onUIAction = {
                                    expandState.value = when (expandState.value) {
                                        UIState.Expand.Collapsed -> UIState.Expand.Expanded
                                        UIState.Expand.Expanded -> UIState.Expand.Collapsed
                                    }
                                }
                            )
                        }
                    }
                } else {
                    data.collapsedIcon?.let {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp)
                        ) {
                            SmallIconAtm(
                                data = it,
                                onUIAction = {
                                    expandState.value = when (expandState.value) {
                                        UIState.Expand.Collapsed -> UIState.Expand.Expanded
                                        UIState.Expand.Expanded -> UIState.Expand.Collapsed
                                    }
                                })
                        }
                    }
                }
            }
            AnimatedVisibility(visible = expandState.value == UIState.Expand.Expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    data.expandedContent?.forEachIndexed { index, item ->
                        when (item) {
                            is TableItemHorizontalMlcData -> {
                                TableItemHorizontalMlc(
                                    modifier = Modifier,
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is TableItemVerticalMlcData -> {
                                TableItemVerticalMlc(
                                    modifier = Modifier,
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            else -> {
                                //nothing
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            if (expandState.value == UIState.Expand.Expanded) {
                Spacer(modifier = Modifier.height(0.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

data class AccordionOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ACCORDION_ORG,
    val componentId: UiText? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
    val heading: String? = null,
    val description: String? = null,
    val state: Boolean? = null,
    val expandedIcon: SmallIconAtmData? = null,
    val collapsedIcon: SmallIconAtmData? = null,
    val expandedContent: List<TableBlockItem>? = null
) : TableBlockItem, UIElementData

fun AccordionOrg?.toUIModel(): AccordionOrgData {
    val entity = this
    val expandedContent = mutableListOf<TableBlockItem>().apply {
        entity?.expandedContent?.items?.forEach { listMlc ->
            if (listMlc.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add((listMlc.tableItemHorizontalMlc as TableItemHorizontalMlc).toUiModel())
            }
            if (listMlc.tableItemVerticalMlc is TableItemVerticalMlc) {
                add((listMlc.tableItemVerticalMlc as TableItemVerticalMlc).toUiModel())
            }
        }
    }
    return AccordionOrgData(
        componentId = UiText.DynamicString(this?.componentId.orEmpty()),
        paddingTop = this?.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this?.paddingMode?.side.toSidePaddingMode(),
        heading = this?.heading,
        description = this?.description,
        state = this?.states?.isExpanded,
        expandedIcon = SmallIconAtmData(
            componentId = this?.states?.expandedIcon?.componentId.toDynamicString(),
            code = this?.states?.expandedIcon?.code ?: "",
            accessibilityDescription = this?.states?.expandedIcon?.accessibilityDescription
        ),
        collapsedIcon = SmallIconAtmData(
            componentId = this?.states?.collapsedIcon?.componentId.toDynamicString(),
            code = this?.states?.collapsedIcon?.code ?: "",
            accessibilityDescription = this?.states?.collapsedIcon?.accessibilityDescription
        ),
        expandedContent = expandedContent
    )
}

@Composable
@Preview
fun AccordionOrgExpandedPreview() {
    val items = listOf(
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
        TableItemVerticalMlcData(
            id = "02",
            title = UiText.DynamicString("Номер:"),
            secondaryTitle = UiText.DynamicString("XX000000")
        ),
    )
    val startState = AccordionOrgData(
        paddingTop = TopPaddingMode.MEDIUM,
        paddingHorizontal = SidePaddingMode.MEDIUM,
        heading = "Heading",
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
        expandedContent = items
    )

    AccordionOrg(
        data = startState,
        onUIAction = {}
    )
}

@Composable
@Preview
fun AccordionOrgCollapsedPreview() {
    val items = listOf(
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
            value = "Value_03"
        ),
        TableItemHorizontalMlcData(
            id = "4",
            title = UiText.DynamicString("Item title 4"),
            value = "Value_04"
        ),
        TableItemHorizontalMlcData(
            id = "5",
            title = UiText.DynamicString("Item title 5"),
            value = "Value_05"
        ),
    )

    val startState = AccordionOrgData(
        paddingTop = TopPaddingMode.NONE,
        paddingHorizontal = SidePaddingMode.NONE,
        heading = "Heading",
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
        expandedContent = items
    )

    AccordionOrg(
        modifier = Modifier.padding(16.dp),
        data = startState,
        onUIAction = {}
    )
}