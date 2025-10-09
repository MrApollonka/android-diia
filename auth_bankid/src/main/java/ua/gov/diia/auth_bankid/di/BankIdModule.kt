package ua.gov.diia.auth_bankid.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import retrofit2.Retrofit
import ua.gov.diia.auth_bankid.BankIdConst.VERIFICATION_METHOD_BANK_ID
import ua.gov.diia.auth_bankid.BankIdConst.VERIFICATION_METHOD_MONO
import ua.gov.diia.auth_bankid.BankIdConst.VERIFICATION_METHOD_PRIVAT
import ua.gov.diia.auth_bankid.network.ApiBankId
import ua.gov.diia.auth_bankid.util.VerificationMethodBankId
import ua.gov.diia.auth_bankid.util.VerificationMethodMono
import ua.gov.diia.auth_bankid.util.VerificationMethodPrivat
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.ProlongClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.verification.di.ProviderVerifiedClient
import ua.gov.diia.verification.di.VerificationProviderType
import ua.gov.diia.verification.ui.methods.VerificationMethod

@Module
@InstallIn(SingletonComponent::class)
interface BankIdModule {

    companion object {

        @Provides
        @UnauthorizedClient
        fun provideApiBankIdUnauthorized(
            @UnauthorizedClient retrofit: Retrofit
        ): ApiBankId = retrofit.create(ApiBankId::class.java)

        @Provides
        @ProlongClient
        fun provideApiBankIdProlong(
            @ProlongClient retrofit: Retrofit
        ): ApiBankId = retrofit.create(ApiBankId::class.java)

        @Provides
        @AuthorizedClient
        fun provideApiBankIdAuthorized(
            @AuthorizedClient retrofit: Retrofit
        ): ApiBankId = retrofit.create(ApiBankId::class.java)

        @Provides
        @ProviderVerifiedClient
        fun provideApiBankId(
            providerType: VerificationProviderType,
            @AuthorizedClient apiBankIdAuthorized: ApiBankId,
            @UnauthorizedClient apiBankIdUnauthorized: ApiBankId,
            @ProlongClient apiBankIdProlong: ApiBankId
        ): ApiBankId = when (providerType) {
            VerificationProviderType.AUTHORIZED -> apiBankIdAuthorized
            VerificationProviderType.UNAUTHORIZED -> apiBankIdUnauthorized
            VerificationProviderType.PROLONG -> apiBankIdProlong
        }
    }

    @Binds
    @IntoMap
    @StringKey(VERIFICATION_METHOD_BANK_ID)
    fun bindVerificationMethodBankId(method: VerificationMethodBankId): VerificationMethod

    @Binds
    @IntoMap
    @StringKey(VERIFICATION_METHOD_MONO)
    fun bindVerificationMethodMono(method: VerificationMethodMono): VerificationMethod

    @Binds
    @IntoMap
    @StringKey(VERIFICATION_METHOD_PRIVAT)
    fun bindVerificationMethodPrivat(method: VerificationMethodPrivat): VerificationMethod

}