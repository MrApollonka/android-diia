package ua.gov.diia.core.data.data_source.network.api

import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.gov.diia.core.models.appversion.AppSettingsInfo
import ua.gov.diia.core.network.annotation.Analytics

interface ApiSettings {

    @Analytics("getAppSettingsInfo")
    @GET("api/v1/settings")
    suspend fun appSettingsInfo(
        @QueryMap deviceFeatures: Map<String, String> = emptyMap()
    ): AppSettingsInfo

}