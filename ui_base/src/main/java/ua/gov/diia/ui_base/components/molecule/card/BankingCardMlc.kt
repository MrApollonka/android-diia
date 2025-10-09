package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.core.models.common_compose.mlc.card.BankingCardMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.MediumIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUIModel
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.Alabaster
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha60
import ua.gov.diia.ui_base.components.theme.WhiteAlpha70
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BankingCardMlc(
    modifier: Modifier = Modifier,
    data: BankingCardMlcData,
    clickable: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    val defaultCardType = data.gradient == null && data.image == null
    Box(
        modifier = modifier
            .height(208.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(WhiteAlpha70)
            .conditional(clickable) {
                noRippleClickable {
                    if (data.action != null) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = data.id,
                                action = data.action
                            )
                        )
                    }
                }
            }
    ) {
        data.gradient?.let {
            CardBackground(animated = true)
        }

        data.image?.let { lImageUrl ->
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Alabaster),
                model = lImageUrl,
                contentDescription = null,
                loading = {
                    BankingCardPlaceholder()
                },
                success = {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                },
                error = {
                    BankingCardPlaceholder()
                }
            )
        }

        Row(
            modifier = modifier
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    data.title?.let {
                        Text(
                            text = it.asString(),
                            style = DiiaTextStyle.h2MediumHeading,
                            color = if (defaultCardType) Black else White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    data.cardNumMask?.let {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = it.asString(),
                            style = DiiaTextStyle.h4ExtraSmallHeading,
                            color = if (defaultCardType) Black else White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    data.expirationDate?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = it.asString(),
                            style = DiiaTextStyle.t1BigText,
                            color = if (defaultCardType) Black else WhiteAlpha60,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    data.description?.let {
                        Text(
                            modifier = Modifier.padding(top = 24.dp),
                            text = data.description.asString(),
                            style = DiiaTextStyle.t1BigText,
                            color = if (defaultCardType) Black else White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                data.logos?.let {
                    Row(
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        it.forEach { item ->
                            ua.gov.diia.ui_base.components.atom.icon.MediumIconAtm(
                                modifier = Modifier.padding(end = 8.dp),
                                data = item,
                                isParentActionAvailable = data.action != null,
                            ) {
                                if (data.action != null) {
                                    onUIAction(
                                        UIAction(
                                            actionKey = data.actionKey,
                                            data = data.id,
                                            action = data.action
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            data.paymentSystemLogo?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(height = 28.dp, width = 68.dp)
                        .focusable(),
                    painter = painterResource(
                        id = DiiaResourceIcon.getResourceId(it.code)
                    ),
                    contentDescription = stringResource(id = DiiaResourceIcon.getContentDescription(it.code))
                )
            }
        }
    }
}

@Composable
private fun BankingCardPlaceholder() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_black))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
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

data class BankingCardMlcData(
    val actionKey: String = UIActionKeysCompose.BANKING_CARD_MLC,
    val componentId: String? = "",
    override val id: String = "",
    val title: UiText? = null,
    val image: String? = null, //url
    val gradient: String? = null, //code
    val paymentSystemLogo: UiIcon.DrawableResource? = null,
    val cardNumMask: UiText? = null,
    val expirationDate: UiText? = null,
    val logos: List<MediumIconAtmData>? = null,
    val description: UiText? = null,
    val action: DataActionWrapper? = null
) : SimpleCarouselCard, SimplePagination

fun BankingCardMlc.toUIModel(): BankingCardMlcData {
    val entity: BankingCardMlc = this
    return BankingCardMlcData(
        componentId = this.componentId,
        id = this.id ?: this.componentId ?: "bankingCardMlc",
        title = this.title?.let { UiText.DynamicString(it) },
        image = this.image,
        gradient = this.gradient,
        paymentSystemLogo = this.paymentSystemLogo?.let { UiIcon.DrawableResource(it) },
        cardNumMask = this.cardNumMask?.let { UiText.DynamicString(it) },
        expirationDate = this.expirationDate?.let { UiText.DynamicString(it) },
        logos = SnapshotStateList<MediumIconAtmData>().apply {
            entity.logos?.forEach { item ->
                item.mediumIconAtm?.let {
                    add(it.toUIModel())
                }
            }
        },
        description = this.description?.let { UiText.DynamicString(it) },
        action = entity.action?.toDataActionWrapper()
    )
}

@Composable
fun CardBackground(
    animated: Boolean?
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gradient_bg_bank_card))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    AnimatedVisibility(
        visible = animated == true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds,
            composition = composition,
            progress = { progress },

            )
    }
    AnimatedVisibility(
        visible = animated == null || animated == false,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bg_blue_yellow_gradient_with_bottom),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
@Preview
fun BankingCardMlcPreview() {
    val state = BankingCardMlcData(
        title = "Дія.Картка".toDynamicString(),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
        paymentSystemLogo = UiIcon.DrawableResource(DiiaResourceIcon.CARD_TYPE__PROSTIR.code),
        cardNumMask = "4909 •••• •••• 9876".toDynamicString(),
        expirationDate = "01/29".toDynamicString(),
        logos = listOf(
            MediumIconAtmData(
                id = "1",
                code = DiiaResourceIcon.LOGO_DIIA_STROKE.code,
                accessibilityDescription = "Button logo 1"
            ),
            MediumIconAtmData(
                id = "2",
                code = DiiaResourceIcon.PRIVAT_BANK.code,
                accessibilityDescription = "Button logo 2"
            ),
        )
    )
    BankingCardMlc(data = state) {}
}

@Composable
@Preview
fun BankingCardMlc_Default_Preview() {
    val state = BankingCardMlcData(
        title = "Дія.Картка".toDynamicString(),
        gradient = "gradient",
        description = "Натисніть, щоб відкрити карту для державної адресної допомоги.".toDynamicString(),
        logos = listOf(
            MediumIconAtmData(
                id = "1",
                code = DiiaResourceIcon.LOGO_DIIA.code,
                accessibilityDescription = "Button logo 1"
            ),
            MediumIconAtmData(
                id = "2",
                code = DiiaResourceIcon.MEDIUM_PLUS.code,
                accessibilityDescription = "Button logo 2"
            ),
        )
    )
    BankingCardMlc(
        data = state
    ) {}
}