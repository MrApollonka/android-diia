package ua.gov.diia.ui_base.components.molecule.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.core.models.common_compose.mlc.media.ArticleVideoMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.media.player.ExoPlayerPortraitComposeView
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrgData
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.toUIModel
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.EggshellAlpha80

@Composable
fun ArticleVideoMlc(
    modifier: Modifier = Modifier,
    data: ArticleVideoMlcData,
    inCarousel: Boolean = false,
    clickable: Boolean = true,
    connectivityState: Boolean,
    onUIAction: (UIAction) -> Unit
) {
    val context = LocalContext.current
    if (data.fullScreenVideoOrg != null) {
        var isLoading by remember { mutableStateOf(true) }

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_black))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .testTag(data.componentId?.asString() ?: "")
                .noRippleClickable { onUIAction(UIAction(actionKey = data.actionKey)) }
                .conditional(!inCarousel) {
                    padding(horizontal = 24.dp).padding(top = 24.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp)),
                model = data.thumbnail.orEmpty(),
                contentDescription = context.getString(R.string.accessibility_video_container),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(R.drawable.diia_article_placeholder),
                error = painterResource(R.drawable.diia_article_placeholder),
                onSuccess = {
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )
            if (isLoading) {
                if (data.thumbnail != null) {
                    Box(
                        modifier = Modifier
                            .height(112.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
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
                } else {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(EggshellAlpha80, shape = RoundedCornerShape(18.dp))
                            .noRippleClickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = BlackAlpha50,
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            LoaderCircularEclipse23Subatomic(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            } else {
                Image(
                    modifier = modifier.size(64.dp),
                    painter = painterResource(id = R.drawable.ic_player_btn_atm_play),
                    contentDescription = null
                )
            }
        }
    } else {
        ExoPlayerPortraitComposeView(
            modifier = modifier.testTag(data.componentId?.asString() ?: ""),
            url = data.url,
            inCarousel = inCarousel,
            clickable = clickable,
            connectivityState = connectivityState
        )
    }
}


data class ArticleVideoMlcData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_VIDEO_MLC,
    override val id: String? = null,
    val url: String? = null,
    val thumbnail: String? = null,
    val componentId: UiText? = null,
    val fullScreenVideoOrg: FullScreenVideoOrgData? = null,
) : SimpleCarouselCard

fun ArticleVideoMlc.toUiModel(): ArticleVideoMlcData {
    return ArticleVideoMlcData(
        url = source,
        thumbnail = thumbnail,
        componentId = this.componentId.toDynamicStringOrNull(),
        fullScreenVideoOrg = this.fullScreenVideoOrg?.toUIModel()
    )
}

fun generateArticleVideoMlcMockData(): ArticleVideoMlcData {
    return ArticleVideoMlcData(url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")
}

fun generateArticleVideoMlcFullScreenMockData(): ArticleVideoMlcData {
    return ArticleVideoMlcData(
        url = null,
        thumbnail = "https://picsum.photos/id/1/400/200",
        fullScreenVideoOrg = FullScreenVideoOrgData(
            source = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        )
    )
}

@Preview
@Composable
fun ArticleVideoAtmPreview() {
    val state = remember { mutableStateOf(true) }
    ArticleVideoMlc(
        modifier = Modifier,
        data = generateArticleVideoMlcMockData(),
        connectivityState = state.value
    ) {}
}


@Preview
@Composable
fun ArticleVideoAtmPreview_full_screen() {
    val state = remember { mutableStateOf(true) }
    ArticleVideoMlc(
        modifier = Modifier,
        data = generateArticleVideoMlcFullScreenMockData(),
        connectivityState = state.value
    ) {}
}