package ua.gov.diia.opensource.payment.delegation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.share.ShareHelper
import ua.gov.diia.payment.delegation.WithShareReceipt
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.payment.models.PaymentReceiptUrl
import ua.gov.diia.payment.models.ReceiptAction
import ua.gov.diia.payment.repository.PaymentRepository
import javax.inject.Inject

class DownloadAndShareReceipt @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val shareHelper: ShareHelper
) : WithShareReceipt {

    override val openingReceipt = MutableLiveData<Pair<String, Boolean>>()

    override val sharingReceipt = MutableLiveData<Pair<String, Boolean>>()

    override val openDocument = MutableLiveData<UiDataEvent<String>>()

    override val shareDocument = MutableLiveData<UiDataEvent<ShareByteArr>>()

    override val stopLoadingIndicator = MutableLiveData<UiDataEvent<Boolean>>()

    override fun <T> T.prepareReceipt(
        paymentFlow: PaymentFlow,
        id: String,
        receiptAction: ReceiptAction
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
        /* no-op */
    }

    private fun <T> T.processReceipt(
        receiptAction: ReceiptAction,
        id: String,
        progressIndicator: MutableLiveData<Pair<String, Boolean>>,
        receipt: PaymentReceiptUrl?
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
        /* no-op */
    }

}