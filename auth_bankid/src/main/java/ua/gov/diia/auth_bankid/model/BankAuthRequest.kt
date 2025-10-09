package ua.gov.diia.auth_bankid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankAuthRequest(
    val authUrl: String,
    val bankCode: String
) : Parcelable