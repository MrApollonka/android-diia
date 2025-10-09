package ua.gov.diia.faq.network

import retrofit2.http.GET
import ua.gov.diia.faq.model.Faq

interface ApiFaq {

    @GET("api/v1/faq")
    suspend fun getFaq(): Faq

}