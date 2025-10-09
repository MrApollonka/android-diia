package ua.gov.diia.opensource.payment.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import ua.gov.diia.payment.helper.PaymentNavHelper
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.ui_base.fragments.errordialog.DialogError

class PaymentNavHelperImpl : PaymentNavHelper {

    override fun closePaymentFlow(
        fragment: Fragment,
        paymentFlow: PaymentFlow,
        action: String,
        navController: NavController
    ) {
        /* no-op */
    }

    override fun navigateToErrorD(
        fragment: Fragment,
        error: DialogError
    ) {
        /* no-op */
    }

}