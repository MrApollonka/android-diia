package ua.gov.diia.faq.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.faq.model.Faq
import ua.gov.diia.faq.network.ApiFaq

@Module
@InstallIn(SingletonComponent::class)
object FaqModule {

    @Provides
    @AuthorizedClient
    fun provideApiFaq(
        @AuthorizedClient retrofit: Retrofit
    ): ApiFaq = retrofit.create(ApiFaq::class.java)


    @Provides
    @MoshiAdapterFaq
    fun provideMoshiAdapterFaq(): JsonAdapter<Faq> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(Faq::class.java)

}