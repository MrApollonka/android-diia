package ua.gov.diia.payment.delegation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.ReceiptAction

interface WithShareReceipt {

    val openingReceipt: LiveData<Pair<String, Boolean>>

    val sharingReceipt: LiveData<Pair<String, Boolean>>

    val openDocument: LiveData<UiDataEvent<String>>

    val shareDocument: LiveData<UiDataEvent<ShareByteArr>>

    val stopLoadingIndicator: LiveData<UiDataEvent<Boolean>>

    fun <T> T.prepareReceipt(
        paymentFlow: PaymentFlow,
        id: String,
        receiptAction: ReceiptAction = ReceiptAction.OPEN
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction

}