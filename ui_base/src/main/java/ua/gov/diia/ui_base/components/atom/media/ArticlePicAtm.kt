package ua.gov.diia.ui_base.components.atom.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.core.models.notification.pull.message.ArticlePicAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination

/**
 * @param isPortrait is mandatory, after any editing, review how composition behaves
 * in organisms. For example, PhotoGroupOrg
 */
@Composable
fun ArticlePicAtm(
    modifier: Modifier = Modifier,
    data: ArticlePicAtmData,
    inCarousel: Boolean = false,
    clickable: Boolean = true,
    isPortrait: Boolean = false, //do not remove
    contentDescriptor: UiText? = null,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .conditional(!inCarousel) {
                padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (isPortrait) 3f / 4f else 16f / 9f)
        ) {
            var isLoading by remember(data.url) { mutableStateOf(true) }

            val contentDesc = contentDescriptor?.asString() ?: ""
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.TopCenter)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .clearAndSetSemantics {
                        role = Role.Image
                        if (data.accessibilityDescription != null || contentDescriptor != null) {
                            contentDescription = data.accessibilityDescription ?: contentDesc
                        } else {
                            hideFromAccessibility()
                        }
                    }
                    .conditional(clickable) {
                        noRippleClickable {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = data.id
                                )
                            )
                        }
                    },
                model = data.url,
                contentDescription = contentDescriptor?.asString(),
                contentScale = data.contentScale,
                onSuccess = {
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )
            if (isLoading && !isPortrait) {
                Box(
                    modifier = Modifier
                        .matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageCardPlaceholder()
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
            .clip(shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.diia_article_placeholder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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

data class ArticlePicAtmData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_PIC_ATM,
    override val id: String,
    val url: String? = null,
    val accessibilityDescription: String? = null,
    val contentScale: ContentScale = ContentScale.Fit,
    val componentId: UiText? = null
) : SimpleCarouselCard, SimplePagination

fun ArticlePicAtm?.toUiModel(): ArticlePicAtmData? {
    if (this == null) return null
    return ArticlePicAtmData(
        url = this.image,
        accessibilityDescription = this.accessibilityDescription,
        componentId = this.componentId.toDynamicStringOrNull(),
        id = this.componentId ?: ""
    )
}

enum class ArticlePicAtmMockType {
    valid, invalid
}

fun generateArticlePicAtmMockData(mockType: ArticlePicAtmMockType): ArticlePicAtmData {
    return when (mockType) {
        ArticlePicAtmMockType.valid -> ArticlePicAtmData(
            url = "https://deep-image.ai/blog/content/images/2022/09/underwater-magic-world-8tyxt9yz.jpeg",
            id = "123"
        )

        ArticlePicAtmMockType.invalid -> ArticlePicAtmData(
            url = "https://business.diia.gov.ua/uplosdfsdfads/4/22881-main.jpg",
            id = "123"
        )
    }
}

@Preview
@Composable
fun ArticlePicAtmPreview() {
    ArticlePicAtm(
        modifier = Modifier, data = generateArticlePicAtmMockData(ArticlePicAtmMockType.valid)
    ) {

    }
}

@Preview
@Composable
fun ArticlePicAtmPreview_Invalid() {
    ArticlePicAtm(
        modifier = Modifier, data = generateArticlePicAtmMockData(ArticlePicAtmMockType.invalid)
    ) {

    }
}