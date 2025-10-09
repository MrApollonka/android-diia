package ua.gov.diia.pin.ui.create.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganism
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.models.orientation.Orientation


@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Orientation.Landscape
    } else {
        Orientation.Portrait
    }

    when (orientation) {
        Orientation.Portrait -> CreatePinScreenPortrait(modifier, data, onUIAction)
        Orientation.Landscape -> CreatePinScreenLandscape(modifier, data, onUIAction)
    }
}

@Composable
fun CreatePinScreenPortrait(
    modifier: Modifier,
    data: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .safeDrawingPadding()
            .provideTestTagsAsResourceId()
    ) {
        val title = createRef()
        val numButton = createRef()
        val descriptionText = createRef()
        data.forEach { item ->
            if (item is TopGroupOrgData) {
                val data = (item as TopGroupOrgData)
                val topGroupOrgContentDescription = data.titleGroupMlcData?.heroText?.asString() ?: ""
                TopGroupOrg(
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .clearAndSetSemantics {
                            contentDescription = topGroupOrgContentDescription
                        }
                        .focusRequester(focusRequester)
                        .focusable(),
                    data = item,
                    onUIAction = onUIAction
                )
            }

            if (item is TextLabelMlcData) {
                TextLabelMlc(
                    modifier = modifier
                        .constrainAs(descriptionText) {
                            top.linkTo(title.bottom)
                        },
                    data = item,
                    onUIAction = onUIAction
                )
            }
            if (item is NumButtonTileOrganismData) {
                NumButtonTileOrganism(
                    modifier = Modifier
                        .constrainAs(numButton) {
                            linkTo(
                                top = descriptionText.bottom,
                                bottom = parent.bottom,
                                bias = 0.3f
                            )
                        },
                    data = item,
                    orientation = Orientation.Portrait,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

@Composable
fun CreatePinScreenLandscape(
    modifier: Modifier,
    data: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .safeDrawingPadding()
            .provideTestTagsAsResourceId()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            data.forEach { item ->
                if (item is TopGroupOrgData) {
                    TopGroupOrg(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                if (item is TextLabelMlcData) {
                    TextLabelMlc(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            data.forEach { item ->
                if (item is NumButtonTileOrganismData) {
                    NumButtonTileOrganism(
                        modifier = Modifier,
                        data = item,
                        orientation = Orientation.Landscape,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CreatePinScreenPreview() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Повторіть код з 4 цифр"),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = DiiaResourceIcon.BACK.code,
                    accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                    action = DataActionWrapper(
                        type = "back",
                        subtype = null,
                        resource = null
                    )
                )
            )
        )
    )
    _uiData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Цей код ви будете вводити для входу у застосунок Дія.")
        )
    )
    _uiData.add(
        NumButtonTileOrganismData()
    )
    CreatePinScreen(data = uiData, onUIAction = { })
}

@Composable
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
fun CreatePinScreenPreview_small_screen() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Повторіть код з 4 цифр"),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = DiiaResourceIcon.BACK.code,
                    accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                    action = DataActionWrapper(
                        type = "back",
                        subtype = null,
                        resource = null
                    )
                )
            )
        )
    )
    _uiData.add(
        TextWithParametersData(
            text = UiText.DynamicString("Цей код ви будете вводити для входу у застосунок Дія.")
        )
    )
    _uiData.add(
        NumButtonTileOrganismData(pinLength = 5)
    )
    CreatePinScreen(data = uiData, onUIAction = { })
}


@Composable
@Preview(heightDp = 360, widthDp = 800)
fun CreatePinScreenLandscapePreview() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Повторіть код з 4 цифр"),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = DiiaResourceIcon.BACK.code,
                    accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                    action = DataActionWrapper(
                        type = "back",
                        subtype = null,
                        resource = null
                    )
                )
            )
        )
    )
    _uiData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Цей код ви будете вводити для входу у застосунок Дія.")
        )
    )
    _uiData.add(
        NumButtonTileOrganismData()
    )
    CreatePinScreenLandscape(modifier = Modifier, data = uiData, onUIAction = { })
}