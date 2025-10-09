package ua.gov.diia.payment.helper

import android.content.Context

interface PaymentLaunchHelper {

    fun startPaymentFlow(
        context: Context,
        json: String,
        result: (resultCode: String?) -> Unit
    )

}