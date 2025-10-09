package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.carousel.BankingCardCarouselOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.MediumIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.card.BankingCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.toUIModel

@Composable
fun BankingCardCarouselOrg(
    modifier: Modifier = Modifier,
    data: BankingCardCarouselOrgData,
    onUIAction: (UIAction) -> Unit
) {
    BaseSimpleCarouselInternal(
        modifier = modifier.padding(top = 16.dp),
        data = data,
        onUIAction = {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = it.data,
                    action = it.action
                )
            )
        }
    )
}

data class BankingCardCarouselOrgData(
    val actionKey: String = UIActionKeysCompose.BANKING_CARD_CAROUSEL_ORG,
    val id: String? = null,
    override val items: List<SimpleCarouselCard>,
) : BaseSimpleCarouselInternalData

fun BankingCardCarouselOrg.toUiModel(): BankingCardCarouselOrgData {
    return BankingCardCarouselOrgData(
        id = this.id,
        items = mutableListOf<SimpleCarouselCard>().apply {
            items.forEach {
                it.bankingCardMlc?.let { bcMlc ->
                    add(bcMlc.toUIModel())
                }
            }
        } as List<SimpleCarouselCard>
    )
}

@Preview
@Composable
fun BankingCardCarouselOrgPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        val card = BankingCardMlcData(
            title = "Дія.Картка".toDynamicString(),
            image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
            paymentSystemLogo = UiIcon.DrawableResource(DiiaResourceIcon.LOGO_VISA_WHITE.code),
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
        val cardDefault = BankingCardMlcData(
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
        val cards = listOf(card, card, card, cardDefault)
        val data = BankingCardCarouselOrgData(
            items = cards
        )
        BankingCardCarouselOrg(data = data) {
        }
    }
}