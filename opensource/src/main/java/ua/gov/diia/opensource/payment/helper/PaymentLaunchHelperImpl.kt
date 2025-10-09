package ua.gov.diia.opensource.payment.helper

import android.content.Context
import ua.gov.diia.payment.helper.PaymentLaunchHelper

class PaymentLaunchHelperImpl : PaymentLaunchHelper {

    override fun startPaymentFlow(
        context: Context,
        json: String,
        result: (String?) -> Unit
    ) {
        /* no-op */
    }

}