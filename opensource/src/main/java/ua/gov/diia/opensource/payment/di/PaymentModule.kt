package ua.gov.diia.opensource.payment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.payment.helper.PaymentLaunchHelperImpl
import ua.gov.diia.opensource.payment.helper.PaymentNavHelperImpl
import ua.gov.diia.opensource.payment.repository.PaymentRepositoryImpl
import ua.gov.diia.payment.helper.PaymentLaunchHelper
import ua.gov.diia.payment.helper.PaymentNavHelper
import ua.gov.diia.payment.repository.PaymentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PaymentModule {

    @Provides
    @Singleton
    fun providePaymentRepository(): PaymentRepository = PaymentRepositoryImpl()

    @Provides
    @Singleton
    fun providePaymentLaunchHelper(): PaymentLaunchHelper = PaymentLaunchHelperImpl()

    @Provides
    @Singleton
    fun providePaymentNavHelper(): PaymentNavHelper = PaymentNavHelperImpl()

}