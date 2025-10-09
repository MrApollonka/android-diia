package ua.gov.diia.payment.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import ua.gov.diia.payment.models.PaymentFlow
import ua.gov.diia.ui_base.fragments.errordialog.DialogError

interface PaymentNavHelper {

    fun closePaymentFlow(
        fragment: Fragment,
        paymentFlow: PaymentFlow,
        action: String,
        navController: NavController
    )

    fun navigateToErrorD(
        fragment: Fragment,
        error: DialogError
    )

}