package ua.gov.diia.payment.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class PaymentFlow(val code: String) : Parcelable