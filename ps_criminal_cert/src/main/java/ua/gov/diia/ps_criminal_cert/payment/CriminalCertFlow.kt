package ua.gov.diia.ps_criminal_cert.payment

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import ua.gov.diia.payment.models.PaymentFlow

@Keep
@Parcelize
data object CriminalCertFlow : PaymentFlow(code = "criminal-cert")