package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.OutlineButtonMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.SidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.TopPaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.toDp
import ua.gov.diia.ui_base.components.infrastructure.utils.toSidePaddingMode
import ua.gov.diia.ui_base.components.infrastructure.utils.toTopPaddingMode
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.GraySpinnerLoaderSubAtm
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun OutlineButtonMlc(
    modifier: Modifier = Modifier,
    data: OutlineButtonMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .noRippleClickable {
                if ((progressIndicator.first.isNotEmpty() && progressIndicator.first == data.id && progressIndicator.second)) return@noRippleClickable
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .padding(
                start = data.paddingHorizontal.toDp(defaultPadding = 0.dp),
                top = data.paddingTop.toDp(defaultPadding = 0.dp),
                end = data.paddingHorizontal.toDp(defaultPadding = 0.dp)
            )
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = progressIndicator.first == data.id && data.id.isNotEmpty() && progressIndicator.second) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .size(24.dp)
                ) {
                    GraySpinnerLoaderSubAtm()
                }
            }
            AnimatedVisibility(visible = progressIndicator.first != data.id || !progressIndicator.second || data.id.isEmpty()) {
                data.iconLeft?.let {
                    Box(modifier = Modifier.align(Alignment.Bottom)) {
                        SmallIconAtm(data = it)
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t1BigText,
                color = Black
            )
        }
    }
}

data class OutlineButtonMlcData(
    val actionKey: String = UIActionKeysCompose.OUTLINE_BTN_MLC,
    val componentId: UiText? = null,
    val id: String,
    val label: UiText,
    val iconLeft: SmallIconAtmData?,
    val action: DataActionWrapper? = null,
    val paddingTop: TopPaddingMode? = null,
    val paddingHorizontal: SidePaddingMode? = null
) : UIElementData

fun OutlineButtonMlc.toUIModel(id: String? = null): OutlineButtonMlcData {
    return OutlineButtonMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        id = this.componentId ?: id ?: "",
        label = title.toDynamicString(),
        iconLeft = this.iconLeft?.toUiModel(),
        action = action?.toDataActionWrapper(),
        paddingTop = paddingMode?.top.toTopPaddingMode(),
        paddingHorizontal = paddingMode?.side.toSidePaddingMode()
    )
}


@Composable
@Preview
fun OutlineButtonMlcPreview() {
    val data = OutlineButtonMlcData(
        componentId = "component_id".toDynamicString(),
        id = "id",
        label = "Зареєструвати авто".toDynamicString(),
        iconLeft = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
            accessibilityDescription = "Button"
        ),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
    )
    OutlineButtonMlc(data = data) {

    }
}

