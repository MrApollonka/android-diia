package ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup

import androidx.compose.foundation.background
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
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelPlaneMlc
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrgV2
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrgV2Item
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.molecule.card.GalleryImageMolecule
import ua.gov.diia.ui_base.components.molecule.card.GalleryImageMoleculeData
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
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUiModel
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.AttentionIconMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.BackgroundMode
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlc
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelPlaneMlc
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelPlaneMlcData
import ua.gov.diia.ui_base.components.theme.BlackSqueeze

@Composable
fun TableBlockOrgV2(
    modifier: Modifier = Modifier,
    data: TableBlockOrgV2Data,
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
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.chip?.let {
            ChipStatusAtm(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.chip
            )
        }
        data.headerMain?.let {
            TableMainHeadingMlc(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.headerMain,
                onUIAction = onUIAction
            )
        }
        data.headerSecondary?.let {
            TableSecondaryHeadingMlc(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.headerSecondary,
                onUIAction = onUIAction
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    top = if(data.headerSecondary == null) 16.dp else 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
        ) {
            data.items?.forEachIndexed { index, item ->
                when (item) {
                    is TableItemHorizontalMlcData -> {
                        TableItemHorizontalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is DocTableItemHorizontalMlcData -> {
                        DocTableItemHorizontalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemVerticalMlcData -> {
                        TableItemVerticalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = {
                                onUIAction(
                                    UIAction(
                                        actionKey = it.actionKey,
                                        data = item.id,
                                        action = item.icon?.action
                                    )
                                )
                            }
                        )
                    }

                    is TableItemPrimaryMlcData -> {
                        TableItemPrimaryMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is GalleryImageMoleculeData -> {
                        GalleryImageMolecule(
                            modifier = modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is SmallEmojiPanelMlcData -> {
                        SmallEmojiPanelMlc(
                            modifier = modifier,
                            data = item,
                        )
                    }

                    is SmallEmojiPanelPlaneMlcData -> {
                        SmallEmojiPanelPlaneMlc(
                            modifier = modifier,
                            data = item,
                        )
                    }

                    else -> {
                        //nothing
                    }
                }

                if (index != data.items.size - 1) {
                    DividerSlimAtom(modifier = Modifier.padding(top = 8.dp), color = BlackSqueeze)
                }

                if (index != data.items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        data.attentionIconMessageMlc?.let {
            AttentionIconMessageMlc(
                modifier = Modifier,
                data = data.attentionIconMessageMlc.copy(
                    paddingHorizontal = SidePaddingMode.MEDIUM,
                    paddingTop = TopPaddingMode.LARGE
                ),
                onUIAction = onUIAction
            )
        }
    }
}

data class TableBlockOrgV2Data(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val componentId: UiText? = null,
    val chip: ChipStatusAtmData? = null,
    val headerMain: TableMainHeadingMlcData? = null,
    val headerSecondary: TableSecondaryHeadingMlcData? = null,
    val items: List<TableBlockItem>? = null,
    val attentionIconMessageMlc: AttentionIconMessageMlcData? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null,
) : UIElementData

fun TableBlockOrgV2?.toUIModel(): TableBlockOrgV2Data? {
    val entity = this
    if (entity?.items == null) return null
    val tbItems = mutableListOf<TableBlockItem>().apply {
        (entity.items as List<TableBlockOrgV2Item>).forEach { listMlcl ->
            if (listMlcl.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add(
                    (listMlcl.tableItemHorizontalMlc as TableItemHorizontalMlc).toUiModel()
                )
            }

            if (listMlcl.tableItemVerticalMlc is TableItemVerticalMlc) {
                add(
                    (listMlcl.tableItemVerticalMlc as TableItemVerticalMlc).toUiModel()
                )
            }

            if (listMlcl.tableItemPrimaryMlc is TableItemPrimaryMlc) {
                val item = (listMlcl.tableItemPrimaryMlc as TableItemPrimaryMlc).toUIModel()
                item?.let {
                    add(it)
                }
            }

            if (listMlcl.smallEmojiPanelPlaneMlc is SmallEmojiPanelPlaneMlc) {
                val item = (listMlcl.smallEmojiPanelPlaneMlc as SmallEmojiPanelPlaneMlc)
            }
        }
    }
    return TableBlockOrgV2Data(
        componentId = this?.componentId?.let { UiText.DynamicString(it) },
        chip = this?.chipStatusAtm?.toUiModel(),
        headerMain = this?.tableMainHeadingMlc?.toUIModel(),
        items = tbItems,
        attentionIconMessageMlc = this?.attentionIconMessageMlc?.toUiModel(),
        paddingTop = this?.paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = this?.paddingMode?.side.toSidePaddingMode(),
    )
}

fun generateTableBlockOrgV2Data(): TableBlockOrgV2Data {
    val items = listOf(
        SmallEmojiPanelMlcData(
            text = UiText.DynamicString("Booster vaccine dose"),
            icon = UiIcon.DrawableResource(
                code = DiiaResourceIcon.SYRINGE.code
            )
        ),
        SmallEmojiPanelPlaneMlcData(
            text = UiText.DynamicString("SmallEmojiPanelPlaneMlcData"),
            icon = UiIcon.DrawableResource(
                code = DiiaResourceIcon.PROFILE.code
            )
        ),
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

    val data = TableBlockOrgV2Data(
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        headerMain = TableMainHeadingMlcData(
            title = "Title".toDynamicString(),
            description = "Description".toDynamicString()
        ),
        headerSecondary = "Secondary Header".toDynamicString().toTableSecondaryHeadingMlcData(),
        items = items,
        attentionIconMessageMlc = AttentionIconMessageMlcData(
            icon = SmallIconAtmData(code = DiiaResourceIcon.ELLIPSE_INFO.code),
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            backgroundMode = BackgroundMode.NOTE

        )
    )

    return data
}

fun generateTableBlockOrgV2GalleryData(): TableBlockOrgV2Data{
    val images = listOf(
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/ce1/7fb/thumb_867_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/d01/72e/thumb_868_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/e84/e25/thumb_870_730_410_0_0_auto.jpg",
        "https://go.diia.app/assets/img/pages/screen-phone.png",
    )
    val dataGallery = GalleryImageMoleculeData(
        id = "id",
        images = images
    )

    val data = TableBlockOrgV2Data(
        headerMain = "Фото: ".toDynamicString().toTableMainHeadingMlcData(),
        items = listOf(dataGallery)
    )

    return data
}

@Preview
@Composable
fun TableBlockOrgV2Preview() {
    TableBlockOrgV2(
        modifier = Modifier,
        data = generateTableBlockOrgV2Data()
    ) {}
}

@Preview
@Composable
fun TableBlockOrgV2Preview_gallery() {
    TableBlockOrgV2(
        modifier = Modifier,
        data = generateTableBlockOrgV2GalleryData()
    ) {}
}

@Preview
@Composable
fun TableBlockOrgV2Preview_small_emodji() {
    val emoji = SmallEmojiPanelMlcData(
        text = UiText.DynamicString("Booster vaccine dose"),
        icon = UiIcon.DrawableResource(
            code = DiiaResourceIcon.TRIDENT.code
        )
    )

    val data = TableBlockOrgV2Data(
        items = listOf(emoji)
    )

    TableBlockOrgV2(
        modifier = Modifier,
        data = data
    ) {}
}

@Preview
@Composable
fun TableBlockOrgV2Preview_WithHeader() {
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
    val data = TableBlockOrgV2Data(
        items = items,
        headerMain = "Header".toDynamicString().toTableMainHeadingMlcData(),
        headerSecondary = "Secondary Header".toDynamicString().toTableSecondaryHeadingMlcData()
    )

    TableBlockOrgV2(
        modifier = Modifier,
        data = data
    ) {

    }
}