package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WarningYellow
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun DocCoverMlcLandscape(
    modifier: Modifier,
    data: DocCoverMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .background(
                color = WarningYellow,
                RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            .noRippleClickable { /* swallow click on whole view */ }
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (content, button, stack) = createRefs()

            Column(
                modifier = Modifier.constrainAs(content) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.title.asString(),
                    color = Black,
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
                Text(
                    text = data.text.asString(),
                    modifier = Modifier.padding(top = 16.dp),
                    color = Black,
                    style = DiiaTextStyle.t3TextBody,
                    maxLines = 5
                )
            }

            data.button?.let {
                BtnStrokeAdditionalAtm(
                    modifier = Modifier.constrainAs(button) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    data = data.button,
                    onUIAction = onUIAction
                )
            }

            data.stackMlcData?.let {
                if (data.isStack && data.size != 1) {
                    StackMlc(
                        modifier = Modifier.constrainAs(stack) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }.padding(bottom = 8.dp),
                        data = it,
                        onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_STACK)) }
                    )
                }
            }
        }
    }
}


@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocCoverMlcLandscapePreview() {
    ConstraintLayout(
        modifier = Modifier
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlcLandscape(
            modifier = Modifier.constrainAs(createRef()) {
                end.linkTo(parent.end)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withNoStackButton)
        ) {}
    }

}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocCoverMlcLandscapePreview_WithStackButton() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlcLandscape(
            modifier = Modifier.constrainAs(createRef()) {
                end.linkTo(parent.end)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withStackButton)
        ) {}
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocCoverMlcLandscapePreview_WithStackNoButton() {
    ConstraintLayout(
        modifier = Modifier
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlcLandscape(
            modifier = Modifier.constrainAs(createRef()) {
                end.linkTo(parent.end)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withStackNoButton)
        ) {}
    }
}