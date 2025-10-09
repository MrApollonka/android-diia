package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.opensource.helper.SearchScreenNavigationHelperImpl
import ua.gov.diia.search.helper.SearchScreenNavigationHelper

@Module
@InstallIn(ViewModelComponent::class)
class SearchScreenNavigationModule {

    @Provides
    fun provideSearchScreenNavigationHelper(): SearchScreenNavigationHelper {
        return SearchScreenNavigationHelperImpl()
    }
}