package ua.gov.diia.payment.repository

import ua.gov.diia.payment.models.PaymentDataJson
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.PaymentReceiptsList
import ua.gov.diia.payment.models.PaymentStatusCode
import ua.gov.diia.payment.models.PaymentTemplateAndProcessCode

interface PaymentRepository {

    suspend fun getPaymentReceipt(
        paymentFlow: PaymentFlow,
        id: String
    ): PaymentReceiptsList

    suspend fun getPaymentDataJson(
        paymentFlow: PaymentFlow,
        id: String,
        provider: String
    ): PaymentDataJson

    suspend fun setPaymentStatusCode(
        paymentFlow: PaymentFlow,
        resId: String,
        paymentStatusCode: PaymentStatusCode,
    ): PaymentTemplateAndProcessCode

}