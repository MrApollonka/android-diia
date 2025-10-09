package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData


/***
 * TableBlockPlaneOrgLandscape is an a compose view to display document content in horizontal way
 * All inner views are mapped to TableItemVerticalMlc, stored to list and displayed as grid
 * Grid is displayed manually by splited to chunks of 3 items
 * */
@Composable
fun TableBlockPlaneOrgLandscape(
    modifier: Modifier = Modifier,
    data: TableBlockPlaneOrgData,
    existForBody: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {
        val dataItems = data.items.orEmpty().toMutableList()

        data.headerSecondary?.let { item ->
            dataItems.add(
                0,
                TableItemVerticalMlcData(
                    componentId = item.componentId?.asString().orEmpty(),
                    title = item.title,
                    value = item.description,
                    icon = item.iconAtmData,
                )
            )
        }

        data.headerMain?.let { item ->
            dataItems.add(
                0,
                TableItemVerticalMlcData(
                    componentId = item.componentId?.asString().orEmpty(),
                    title = item.title,
                    value = item.description,
                    icon = item.iconAtmData,
                )
            )
        }

        val items = if ((dataItems.size + 1) % 3 == 0 && dataItems.size > 2) {
            dataItems.add(TableItemVerticalMlcData())
            dataItems
        } else {
            dataItems
        }
        val itemsChunked = items.chunked(3)

        Column(modifier = Modifier.fillMaxWidth()) {
            itemsChunked.forEachIndexed { index, chunk ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    chunk.forEachIndexed { index, item ->
                        if (index != 0) {
                            Spacer(modifier = Modifier.size(4.dp))
                        }

                        when (item) {
                            is TableItemHorizontalMlcData -> {
                                TableItemVerticalMlc(
                                    data = TableItemVerticalMlcData(
                                        id = item.id,
                                        componentId = item.componentId,
                                        supportText = item.supportText,
                                        title = item.title,
                                        secondaryTitle = if (!item.secondaryTitle?.asString()
                                                .isNullOrEmpty()
                                        ) {
                                            item.secondaryTitle
                                        } else null,
                                        value = item.value?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        secondaryValue = item.secondaryValue?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        valueAsBase64String = item.valueAsBase64String,
                                        icon = item.iconRight?.let {
                                            IconAtmData(
                                                code = it.asString(),
                                            )
                                        },
                                    ),
                                    onUIAction = onUIAction,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            is TableItemVerticalMlcData -> {
                                TableItemVerticalMlc(
                                    data = item,
                                    onUIAction = onUIAction,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            is DocTableItemHorizontalLongerMlcData -> {
                                TableItemVerticalMlc(
                                    data = TableItemVerticalMlcData(
                                        id = item.id,
                                        supportText = item.supportText,
                                        title = UiText.DynamicString(item.title.orEmpty()),
                                        secondaryTitle = if (!item.secondaryTitle.isNullOrEmpty()) {
                                            UiText.DynamicString(item.secondaryTitle)
                                        } else null,
                                        value = item.value?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        secondaryValue = item.secondaryValue?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        valueAsBase64String = item.valueAsBase64String,
                                        icon = item.iconRight?.let {
                                            IconAtmData(
                                                code = it.asString(),
                                            )
                                        },
                                    ),
                                    onUIAction = onUIAction,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            is DocTableItemHorizontalMlcData -> {
                                TableItemVerticalMlc(
                                    data = TableItemVerticalMlcData(
                                        id = item.id,
                                        supportText = item.supportText,
                                        title = UiText.DynamicString(item.title.orEmpty()),
                                        secondaryTitle = if (!item.secondaryTitle.isNullOrEmpty()) {
                                            UiText.DynamicString(item.secondaryTitle)
                                        } else null,
                                        value = item.value?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        secondaryValue = item.secondaryValue?.let {
                                            UiText.DynamicString(normalizeText(it))
                                        },
                                        valueAsBase64String = item.valueAsBase64String,
                                        icon = item.iconRight?.let {
                                            IconAtmData(
                                                code = it.asString(),
                                            )
                                        },
                                    ),
                                    onUIAction = onUIAction,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            is TableItemPrimaryMlcData -> {
                                TableItemVerticalMlc(
                                    data = TableItemVerticalMlcData(
                                        id = item.id,
                                        componentId = item.componentId,
                                        title = item.title,
                                        value = item.value,
                                        icon = item.icon,
                                    ),
                                    onUIAction = onUIAction,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            else -> {
                                //nothing
                            }
                        }
                    }
                }
                if (index != itemsChunked.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
}

fun normalizeText(value: String): String {
    return value.trim().replace(Regex("\\r\\n|\\r|\\n"), " ")
}

@Preview(heightDp = 360, widthDp = 800, showBackground = true)
@Composable
fun TableBlockPlaneOrgLandscapePreview() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Тип нерухомого майна:"),
            value = "Будинок"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Частка власності:"),
            value = "1/5"
        ),
        TableItemVerticalMlcData(
            id = "3",
            title = UiText.DynamicString("Адреса:"),
            value = UiText.DynamicString("м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16")
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Номер сертифіката"),
            value = "1234567890",
            iconRight = UiText.StringResource(
                R.drawable.ic_copy
            )
        )
    )
    val data = TableBlockPlaneOrgData(items = items)

    TableBlockPlaneOrgLandscape(
        modifier = Modifier,
        data = data
    ) {

    }
}

@Preview(heightDp = 360, widthDp = 800, showBackground = true)
@Composable
fun TableBlockPlaneOrgLandscapePreview_WithHeader() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Тип нерухомого майна:"),
            value = "Будинок"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Частка власності:"),
            value = "1/5"
        ),
        TableItemVerticalMlcData(
            id = "3",
            title = UiText.DynamicString("Адреса:"),
            value = UiText.DynamicString("м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16")
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Номер сертифіката"),
            value = "1234567890",
            iconRight = UiText.StringResource(
                R.drawable.ic_copy
            )
        )
    )
    val data = TableBlockPlaneOrgData(
        items = items,
        headerMain = "Header".toDynamicString().toTableMainHeadingMlcData()
    )

    TableBlockPlaneOrgLandscape(
        modifier = Modifier,
        data = data
    ) {

    }
}


@Preview(heightDp = 360, widthDp = 800, showBackground = true)
@Composable
fun TableBlockPlaneOrgLandscape_Two_Elements_Preview() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Тип нерухомого майна:"),
            value = "Будинок"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Частка власності:"),
            value = "1/5"
        )
    )
    val data = TableBlockPlaneOrgData(items = items)

    TableBlockPlaneOrgLandscape(
        modifier = Modifier,
        data = data
    ) {

    }
}