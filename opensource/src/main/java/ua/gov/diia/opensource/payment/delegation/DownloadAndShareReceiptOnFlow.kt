package ua.gov.diia.opensource.payment.delegation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.share.ShareHelper
import ua.gov.diia.payment.delegation.WithShareReceiptOnFlow
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.PaymentReceiptUrl
import ua.gov.diia.payment.models.ReceiptAction
import ua.gov.diia.payment.repository.PaymentRepository
import javax.inject.Inject

class DownloadAndShareReceiptOnFlow @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val shareHelper: ShareHelper
) : WithShareReceiptOnFlow {

    override val openingReceipt = MutableStateFlow(false)

    override val sharingReceipt = MutableStateFlow(false)

    override val openDocument = mutableSharedFlowOf<UiDataEvent<String>>()

    override val shareDocument = mutableSharedFlowOf<UiDataEvent<ShareByteArr>>()

    override val stopLoadingIndicator = mutableSharedFlowOf<UiDataEvent<Boolean>>()

    override fun <T> T.prepareReceipt(
        paymentFlow: PaymentFlow,
        id: String,
        receiptAction: ReceiptAction
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
        /* no-op */

    }

    private fun <T> T.processReceipt(
        receiptAction: ReceiptAction,
        progressIndicator: MutableSharedFlow<Boolean>,
        receipt: PaymentReceiptUrl?
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
        /* no-op */
    }

}