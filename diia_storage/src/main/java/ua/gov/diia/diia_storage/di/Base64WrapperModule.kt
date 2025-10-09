package ua.gov.diia.diia_storage.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.decoder.AndroidBase64Wrapper
import ua.gov.diia.core.util.decoder.Base64Wrapper

@Module
@InstallIn(SingletonComponent::class)
object Base64WrapperModule {

    @Provides
    fun provideBase64Wrapper(): Base64Wrapper = AndroidBase64Wrapper()

}