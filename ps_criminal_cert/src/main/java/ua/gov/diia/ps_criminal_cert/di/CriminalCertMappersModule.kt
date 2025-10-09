package ua.gov.diia.ps_criminal_cert.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.ps_criminal_cert.ui.steps.nationality.CriminalCertNationalitiesMapper
import ua.gov.diia.ps_criminal_cert.ui.steps.nationality.CriminalCertNationalitiesMapperImpl

@Module
@InstallIn(SingletonComponent::class)
interface CriminalCertMappersModule {

    @Binds
    fun bindCriminalCertNationalitiesMapper(impl: CriminalCertNationalitiesMapperImpl): CriminalCertNationalitiesMapper
}