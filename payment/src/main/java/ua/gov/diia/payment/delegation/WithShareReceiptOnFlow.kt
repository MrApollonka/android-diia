package ua.gov.diia.payment.delegation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.ReceiptAction

interface WithShareReceiptOnFlow {

    val openingReceipt: StateFlow<Boolean>

    val sharingReceipt: StateFlow<Boolean>

    val openDocument: SharedFlow<UiDataEvent<String>>

    val shareDocument: SharedFlow<UiDataEvent<ShareByteArr>>

    val stopLoadingIndicator: SharedFlow<UiDataEvent<Boolean>>

    fun <T> T.prepareReceipt(
        paymentFlow: PaymentFlow,
        id: String,
        receiptAction: ReceiptAction = ReceiptAction.OPEN
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction

}