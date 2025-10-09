package ua.gov.diia.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.search.ui.search_ds.SearchScreenMapper
import ua.gov.diia.search.ui.search_ds.SearchScreenMapperImpl

@Module
@InstallIn(ViewModelComponent::class)
interface SearchScreenDelegationModule {

    @Binds
    fun bindStatusMapper(
        impl: SearchScreenMapperImpl
    ): SearchScreenMapper

}