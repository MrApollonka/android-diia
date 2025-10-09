package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.ExtraLargeIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.ContainerType
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalLargeMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.message.FinalScreenBlockMlc
import ua.gov.diia.ui_base.components.molecule.message.FinalScreenBlockMlcData
import ua.gov.diia.ui_base.components.molecule.text.PaymentInfoOrgV2
import ua.gov.diia.ui_base.components.molecule.text.PaymentInfoOrgV2Data
import ua.gov.diia.ui_base.components.molecule.text.SubTitleCentralizedMlc
import ua.gov.diia.ui_base.components.molecule.text.SubTitleCentralizedMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleCentralizedMlc
import ua.gov.diia.ui_base.components.molecule.text.TitleCentralizedMlcData
import ua.gov.diia.ui_base.components.organism.carousel.PhotoCardCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.PhotoCardCarouselOrgData

@Composable
fun ColumnScope.BodyRootCenteredContainer(
    modifier: Modifier = Modifier,
    centeredBodyViews: SnapshotStateList<UIElementData>,
    containerType: ContainerType = ContainerType.PUBLIC_SERVICE,
    loader: Loader = Loader.createComponent(isLoading = false),
    displayBlockDivider: Boolean = false,
    useGradientBg: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()

    val scrollState = rememberScrollState()

    var displayBottomGradient by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = scrollState.canScrollBackward,
        key2 = scrollState.canScrollForward
    ) {
        displayBottomGradient = displayBlockDivider && scrollState.canScrollForward
    }

    Box(
        modifier = modifier
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            centeredBodyViews.forEachIndexed { index, element ->
                when (element) {
                    is PaymentInfoOrgV2Data -> {
                        PaymentInfoOrgV2(
                            modifier = Modifier,
                            data = element,
                        )
                    }

                    is FinalScreenBlockMlcData -> {
                        FinalScreenBlockMlc(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction

                        )
                    }

                    is TitleCentralizedMlcData -> {
                        TitleCentralizedMlc(
                            data = element
                        )
                    }

                    is SubTitleCentralizedMlcData -> {
                        SubTitleCentralizedMlc(
                            data = element
                        )
                    }

                    is PhotoCardCarouselOrgData -> {
                        PhotoCardCarouselOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }
                }
            }
        }
        GradientDividerContentBlock(displayBottomGradient, containerType, useGradientBg)
    }
}


@Preview
@Composable
fun BodyRootCenteredContainerPreview() {
    val _stub = remember { mutableStateListOf<UIElementData>() }
    val stub: SnapshotStateList<UIElementData> = _stub
    _stub.apply {
        addAll(
            listOf(
                FinalScreenBlockMlcData(
                    subtitle = UiText.DynamicString("text"),
                    title = UiText.DynamicString("title"),
                    icon = ExtraLargeIconAtmData(code = DiiaResourceIcon.PLACEHOLDER.code),
                    componentId = null
                ),
                PaymentInfoOrgV2Data(
                    itemsTableBlock = listOf(
                        TableItemHorizontalMlcData(
                            id = "123",
                            title = UiText.DynamicString("title"),
                            value = "1 000.00 грн"
                        ),
                    ),
                    tableItemHorizontalLargeMlc = TableItemHorizontalLargeMlcData(
                        id = "123",
                        title = UiText.DynamicString("title"),
                        value = "1 000.00 грн"
                    ),
                    title = UiText.DynamicString("title"),
                    subtitle = UiText.DynamicString("subtitle"),

                    )
            )
        )
    }

    Column {
        BodyRootCenteredContainer(
            centeredBodyViews = stub
        ) {

        }
    }
}