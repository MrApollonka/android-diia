package ua.gov.diia.opensource.payment.repository

import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.payment.models.PaymentDataJson
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.PaymentReceiptsList
import ua.gov.diia.payment.models.PaymentStatusCode
import ua.gov.diia.payment.models.PaymentTemplateAndProcessCode
import ua.gov.diia.payment.repository.PaymentRepository
import ua.gov.diia.ui_base.components.DiiaResourceIcon

class PaymentRepositoryImpl : PaymentRepository {

    override suspend fun getPaymentReceipt(
        paymentFlow: PaymentFlow,
        id: String
    ): PaymentReceiptsList {
        return PaymentReceiptsList(
            receipts = emptyList(),
            template = templateDialogModel
        )
    }

    override suspend fun getPaymentDataJson(
        paymentFlow: PaymentFlow,
        id: String,
        provider: String
    ): PaymentDataJson {
        return PaymentDataJson(
            dataJson = null,
            template = templateDialogModel
        )
    }

    override suspend fun setPaymentStatusCode(
        paymentFlow: PaymentFlow,
        resId: String,
        paymentStatusCode: PaymentStatusCode
    ): PaymentTemplateAndProcessCode {
        return PaymentTemplateAndProcessCode(
            template = templateDialogModel
        )
    }

    private val templateDialogModel = TemplateDialogModel(
        key = null,
        type = "middleCenterIconBlackButtonAlert",
        isClosable = false,
        data = TemplateDialogData(
            icon = DiiaResourceIcon.ATTENTION_BLACK_ROUND.code,
            title = "Оплата",
            description = "Далі відбувається процес оплати",
            mainButton = TemplateDialogButton(
                name = "Зрозуміло"
            )
        )
    )

}