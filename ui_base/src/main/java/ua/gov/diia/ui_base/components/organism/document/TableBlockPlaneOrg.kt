package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnLinkAtm
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnLinkAtm
import ua.gov.diia.ui_base.components.atom.button.BtnLinkAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel

@Composable
fun TableBlockPlaneOrg(
    modifier: Modifier = Modifier,
    data: TableBlockPlaneOrgData,
    existForBody: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(
                start = if (existForBody) 24.dp else 16.dp,
                top = 24.dp,
                end = if (existForBody) 24.dp else 24.dp,
            )
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.headerMain?.let {
            TableMainHeadingMlc(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                data = data.headerMain,
                onUIAction = onUIAction
            )
        }
        data.headerSecondary?.let {
            TableSecondaryHeadingMlc(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .focusable(),
                data = data.headerSecondary,
                onUIAction = onUIAction
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data.items?.forEachIndexed { index, item ->
                when (item) {
                    is TableItemHorizontalMlcData -> {
                        TableItemHorizontalMlc(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemVerticalMlcData -> {
                        TableItemVerticalMlc(
                            modifier = Modifier.focusable(),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is DocTableItemHorizontalLongerMlcData -> {
                        DocTableItemHorizontalLongerMlc(
                            modifier = Modifier.focusable(),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is DocTableItemHorizontalMlcData -> {
                        DocTableItemHorizontalMlc(
                            modifier = Modifier.focusable(),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemPrimaryMlcData -> {
                        TableItemPrimaryMlc(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is BtnLinkAtmData -> {
                        BtnLinkAtm(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    else -> {
                        //nothing
                    }
                }
                if (index != data.items.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )
                }
            }
        }
    }
}

data class TableBlockPlaneOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val headerMain: TableMainHeadingMlcData? = null,
    val headerSecondary: TableSecondaryHeadingMlcData? = null,
    val items: List<TableBlockItem>? = null,
    val componentId: UiText? = null,
) : UIElementData

fun TableBlockPlaneOrg?.toUIModel(): TableBlockPlaneOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val tbItems = mutableListOf<TableBlockItem>().apply {
        (entity.items as List<Item>).forEach { listMlcl ->
            if (listMlcl.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add(
                    TableItemHorizontalMlcData(
                        componentId = listMlcl.tableItemHorizontalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemHorizontalMlc?.label?.let {
                            UiText.DynamicString(it)
                        },
                        secondaryTitle = listMlcl.tableItemHorizontalMlc?.secondaryLabel?.let {
                            UiText.DynamicString(it)
                        },
                        value = listMlcl.tableItemHorizontalMlc?.value,
                        secondaryValue = listMlcl.tableItemHorizontalMlc?.secondaryValue,
                        supportText = listMlcl.tableItemHorizontalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemHorizontalMlc?.valueImage,
                        iconRight = listMlcl.tableItemHorizontalMlc?.icon?.code?.let {
                            UiText.DynamicString(it)
                        },
                        valueWithParams = if (listMlcl.tableItemHorizontalMlc?.value != null && !listMlcl.tableItemHorizontalMlc?.valueParameters.isNullOrEmpty()) {
                            TextWithParametersData(
                                text = UiText.DynamicString(listMlcl.tableItemHorizontalMlc?.value.orEmpty()),
                                parameters = listMlcl.tableItemHorizontalMlc?.valueParameters?.map {
                                    TextParameter(
                                        data = TextParameter.Data(
                                            name = it.data?.name.toDynamicStringOrNull(),
                                            resource = it.data?.resource.toDynamicStringOrNull(),
                                            alt = it.data?.alt.toDynamicStringOrNull()
                                        ),
                                        type = it.type
                                    )
                                }
                            )
                        } else {
                            null
                        },
                    )
                )
            }

            if (listMlcl.tableItemVerticalMlc is TableItemVerticalMlc) {
                add(
                    TableItemVerticalMlcData(
                        componentId = listMlcl.tableItemVerticalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemVerticalMlc?.label?.let { UiText.DynamicString(it) },
                        secondaryTitle = listMlcl.tableItemVerticalMlc?.secondaryLabel?.let {
                            UiText.DynamicString(it)
                        },
                        value = listMlcl.tableItemVerticalMlc?.value?.let { UiText.DynamicString(it) },
                        secondaryValue = listMlcl.tableItemVerticalMlc?.secondaryValue?.let {
                            UiText.DynamicString(it)
                        },
                        supportText = listMlcl.tableItemVerticalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemVerticalMlc?.valueImage,
                        valueWithParams = if (listMlcl.tableItemVerticalMlc?.value != null && !listMlcl.tableItemVerticalMlc?.valueParameters.isNullOrEmpty()) {
                            TextWithParametersData(
                                text = UiText.DynamicString(listMlcl.tableItemVerticalMlc?.value.orEmpty()),
                                parameters = listMlcl.tableItemVerticalMlc?.valueParameters?.map {
                                    TextParameter(
                                        data = TextParameter.Data(
                                            name = it.data?.name.toDynamicStringOrNull(),
                                            resource = it.data?.resource.toDynamicStringOrNull(),
                                            alt = it.data?.alt.toDynamicStringOrNull()
                                        ),
                                        type = it.type
                                    )
                                }
                            )
                        } else {
                            null
                        },
                    )
                )
            }

            if (listMlcl.tableItemPrimaryMlc is TableItemPrimaryMlc) {
                val item = (listMlcl.tableItemPrimaryMlc as TableItemPrimaryMlc).toUIModel()
                item?.let {
                    add(it)
                }
            }

            if (listMlcl.btnLinkAtm is BtnLinkAtm) {
                val item = (listMlcl.btnLinkAtm as BtnLinkAtm).toUIModel()
                add(item)
            }
        }
    }
    return TableBlockPlaneOrgData(
        headerMain = this?.tableMainHeadingMlc?.toUIModel(),
        items = tbItems
    )
}

@Preview
@Composable
fun TableBlockPlaneOrgPreview() {
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

    TableBlockPlaneOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}

@Preview
@Composable
fun TableBlockPlaneOrgPreview_WithHeader() {
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

    TableBlockPlaneOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}