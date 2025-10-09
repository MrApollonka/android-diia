package ua.gov.diia.pin.ui.input.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.text.Color
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganism
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.mappers.loader.mapToLoader
import ua.gov.diia.ui_base.models.orientation.Orientation

@Composable
fun PinInputScreen(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<UIElementData>,
    contentLoaded: Pair<String, Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Orientation.Landscape
    } else {
        Orientation.Portrait
    }

    when (orientation) {
        Orientation.Portrait -> PinInputScreenPortrait(modifier, data, contentLoaded, onUIAction)
        Orientation.Landscape -> PinInputScreenLandscape(modifier, data, contentLoaded, onUIAction)
    }
}

@Composable
fun PinInputScreenPortrait(
    modifier: Modifier,
    data: SnapshotStateList<UIElementData>,
    contentLoaded: Pair<String, Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = modifier
                .paint(
                    painterResource(id = R.drawable.bg_blue_yellow_gradient),
                    contentScale = ContentScale.FillBounds
                )
                .fillMaxSize()
                .safeDrawingPadding()
                .provideTestTagsAsResourceId()
        ) {
            val title = createRef()
            val numButton = createRef()
            val bottomText = createRef()
            data.forEach { item ->
                if (item is NavigationPanelMlcData) {
                    Text(
                        modifier = Modifier.constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(numButton.top, margin = 40.dp)
                        },
                        text = item.title?.asString() ?: "",
                        style = DiiaTextStyle.h1Heading
                    )
                }

                if (item is NumButtonTileOrganismData) {
                    NumButtonTileOrganism(
                        modifier = Modifier
                            .constrainAs(numButton) {
                                top.linkTo(parent.top, margin = 16.dp)
                                bottom.linkTo(parent.bottom)
                            },
                        data = item,
                        orientation = Orientation.Portrait,
                        onUIAction = onUIAction
                    )
                }

                if (item is TextLabelMlcData) {
                    ClickableText(
                        modifier = modifier
                            .wrapContentSize()
                            .constrainAs(bottomText) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, margin = 30.dp)
                            }
                            .semantics {
                                role = Role.Button
                            },
                        text = AnnotatedString(item.text.asString()),
                        style = DiiaTextStyle.t1BigText,
                    ) {
                        onUIAction(UIAction(actionKey = item.actionKey))
                    }
                }
            }
        }
        TridentLoaderWithUIBlocking(loader = mapToLoader(content = contentLoaded))
    }
}

@Composable
fun PinInputScreenLandscape(
    modifier: Modifier,
    data: SnapshotStateList<UIElementData>,
    contentLoaded: Pair<String, Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .fillMaxSize()
            .safeDrawingPadding()
            .provideTestTagsAsResourceId()
            .padding(horizontal = 24.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach { item ->
                    if (item is NavigationPanelMlcData) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = item.title?.asString() ?: "",
                            style = DiiaTextStyle.heroText,
                        )
                    }

                    if (item is TextLabelMlcData) {
                        ClickableText(
                            modifier = modifier
                                .wrapContentSize()
                                .semantics {
                                    role = Role.Button
                                },
                            text = AnnotatedString(item.text.asString()),
                            style = DiiaTextStyle.t1BigText,
                        ) {
                            onUIAction(UIAction(actionKey = item.actionKey))
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
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
        TridentLoaderWithUIBlocking(loader = mapToLoader(content = contentLoaded))
    }
}

@Composable
@Preview
fun PinInputScreenPreview() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        NavigationPanelMlcData(
            title = UiText.DynamicString("Код для входу"),
            isContextMenuExist = false
        )
    )
    _uiData.add(
        NumButtonTileOrganismData()
    )
    _uiData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Не пам\\'ятаю код для входу")
        )
    )
    PinInputScreen(data = uiData, onUIAction = { }, contentLoaded = "" to true)
}

@Composable
@Preview(heightDp = 360, widthDp = 800)
fun PinInputScreenLandscapePreview() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        NavigationPanelMlcData(
            title = UiText.DynamicString("Код для входу"),
            isContextMenuExist = false
        )
    )
    _uiData.add(
        NumButtonTileOrganismData()
    )
    _uiData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Не пам\\'ятаю код для входу")
        )
    )
    PinInputScreenLandscape(
        data = uiData,
        onUIAction = { },
        contentLoaded = "" to true,
        modifier = Modifier
    )
}