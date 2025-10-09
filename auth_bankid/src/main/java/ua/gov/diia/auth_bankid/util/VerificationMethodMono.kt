package ua.gov.diia.auth_bankid.util

import ua.gov.diia.auth_bankid.BankIdConst.PACKAGE_MONO
import ua.gov.diia.auth_bankid.BankIdConst.VERIFICATION_METHOD_MONO
import ua.gov.diia.auth_bankid.R
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationRequest
import javax.inject.Inject

class VerificationMethodMono @Inject constructor(
    private val applicationInfoProvider: InstalledApplicationInfoProvider
) : VerificationMethod() {

    override val name: String = VERIFICATION_METHOD_MONO

    override val iconResId = R.drawable.ic_mono_bank

    override val titleResId = R.string.bank_mono

    override val descriptionResId = R.string.accessibility_login_screen_bank_list_item3

    override val isAvailable: Boolean
        get() = applicationInfoProvider.applicationExists(PACKAGE_MONO)

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