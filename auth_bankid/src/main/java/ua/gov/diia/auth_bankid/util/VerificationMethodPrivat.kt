package ua.gov.diia.auth_bankid.util

import ua.gov.diia.auth_bankid.BankIdConst.PACKAGE_PRIVAT
import ua.gov.diia.auth_bankid.BankIdConst.VERIFICATION_METHOD_PRIVAT
import ua.gov.diia.auth_bankid.R
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationRequest
import javax.inject.Inject

class VerificationMethodPrivat @Inject constructor(
    private val applicationInfoProvider: InstalledApplicationInfoProvider
) : VerificationMethod() {

    override val name: String = VERIFICATION_METHOD_PRIVAT

    override val iconResId = R.drawable.ic_privat24

    override val titleResId = R.string.bank_privat24

    override val descriptionResId = R.string.accessibility_login_screen_bank_list_item2

    override val isAvailable: Boolean
        get() = applicationInfoProvider.applicationExists(PACKAGE_PRIVAT)

    override suspend fun getVerificationRequest(
        verificationSchema: String,
        processId: String
    ): VerificationRequest {
        val url = api.getAuthUrl(
            verificationMethodCode = name,
            processId = processId,
            bankCode = null
        )
        return VerificationRequest(
            url = url,
            shouldLaunchUrl = true
        )
    }

}