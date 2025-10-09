package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.core.models.common_compose.mlc.card.ImageCardMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDrawableResourceOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.CodGray
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun ImageCardMlc(
    modifier: Modifier = Modifier,
    data: ImageCardMlcData,
    clickable: Boolean = true,
    inCarousel: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .conditional(!inCarousel) {
                padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            }
            .height(200.dp)
            .background(color = Transparent, shape = RoundedCornerShape(16.dp))
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .semantics {
                onClick(label = null, action = null)
            }
            .focusable()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(CodGray),
            model = data.image,
            contentDescription = null,
            loading = {
                ImageCardPlaceholder()
            },
            success = {
                Image(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {
                            role = Role.Image
                        },
                    painter = painter,
                    contentDescription = data.contentDescription.orEmpty(),
                    contentScale = ContentScale.Crop
                )
            },
            error = {
                ImageCardPlaceholder()
            }
        )

        val description = data.title.asString()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = BlackAlpha50,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .semantics(mergeDescendants = true) {
                        contentDescription = description
                        role = Role.Button
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.title.asString(),
                    style = DiiaTextStyle.t1BigText,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 0.dp)
                        .weight(1f)
                        .clearAndSetSemantics { }
                )
                data.iconEnd?.let {
                    UiIconWrapperSubatomic(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
                            .size(24.dp)
                            .clearAndSetSemantics { },
                        icon = it
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageCardPlaceholder() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_white))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(width = 60.dp, height = 60.dp)
                .alpha(0.2f),
            alignment = Alignment.Center,
            contentScale = ContentScale.Inside,
            composition = composition,
            progress = { progress },
        )
    }
}

data class ImageCardMlcData(
    val actionKey: String = UIActionKeysCompose.IMAGE_CARD_MLC,
    override val id: String,
    val title: UiText,
    val iconEnd: UiIcon? = null,
    val image: String,
    val contentDescription: String? = null,
    val action: DataActionWrapper? = null,
    val componentId: UiText? = null
) : SimpleCarouselCard, SimplePagination

fun ImageCardMlc.toUiModel(
    id: String? = null,
    contentDescription: UiText? = null
): ImageCardMlcData {
    return ImageCardMlcData(
        id = id ?: componentId ?: UIActionKeysCompose.IMAGE_CARD_MLC,
        componentId = componentId?.let { UiText.DynamicString(it) },
        title = label.toDynamicString(),
        iconEnd = iconRight.toDrawableResourceOrNull(),
        image = image,
        contentDescription = imageAltText,
        action = action?.toDataActionWrapper()
    )
}

@Composable
@Preview
fun ImageCardMlcPreview() {
    val data = ImageCardMlcData(
        id = "",
        title = UiText.DynamicString("label"),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
    )
    ImageCardMlc(
        modifier = Modifier,
        data = data
    ) {}
}

@Composable
@Preview
fun ImageCardMlcPreview_WithIcon() {
    val data = ImageCardMlcData(
        id = "",
        title = UiText.DynamicString("label label label label label label label"),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
        iconEnd = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code),
    )
    ImageCardMlc(
        modifier = Modifier,
        data = data
    ) {}
}