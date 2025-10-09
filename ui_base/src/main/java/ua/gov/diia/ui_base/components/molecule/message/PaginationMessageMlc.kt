package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.PaginationMessageMlc
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun PaginationMessageMlc(
    modifier: Modifier = Modifier,
    data: PaginationMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .testTag(data.componentId?.asString().orEmpty())
            .fillMaxWidth()
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 16.dp),
                top = data.paddingTop.toDp(defaultPadding = 8.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 16.dp)
            )
    ) {
        Column(
            modifier = modifier.padding(vertical = 24.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            data.title?.let {
                Text(
                    text = it.asString(),
                    style = DiiaTextStyle.h4ExtraSmallHeading,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            data.description?.let {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Text(
                    text = it.asString(),
                    style = DiiaTextStyle.t3TextBody,
                    textAlign = TextAlign.Center,
                )
            }
            data.button?.let { atomData ->
                BtnStrokeAdditionalAtm(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    data = atomData,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

data class PaginationMessageMlcData(
    override val id: String,
    val componentId: UiText? = null,
    val title: UiText?,
    val description: UiText?,
    val button: ButtonStrokeAdditionalAtomData? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData, SimplePagination

fun PaginationMessageMlc.toUIModel(): PaginationMessageMlcData {
    return PaginationMessageMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        id = this.componentId.orEmpty(),
        title = title?.let { UiText.DynamicString(it) },
        description = description?.let { UiText.DynamicString(it) },
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode(),
        button = btnStrokeAdditionalAtm?.toUIModel()
    )
}

fun generatePaginationMessageMlcMockData() = PaginationMessageMlcData(
    id = "",
    title = UiText.DynamicString("Не вдалось завантажити дані, спробуйте повторити ще раз"),
    description = UiText.DynamicString("Error message to notify the user about wrong activity or lack of info."),
    button = ButtonStrokeAdditionalAtomData(
        actionKey = UIActionKeysCompose.BUTTON_REGULAR,
        title = UiText.DynamicString("Оновити"),
        interactionState = UIState.Interaction.Enabled,
    )
)

@Preview
@Composable
fun PaginationMessageMlcPreview() {
    PaginationMessageMlc(
        data = generatePaginationMessageMlcMockData(),
        onUIAction = {
            /* no-op */
        }
    )
}