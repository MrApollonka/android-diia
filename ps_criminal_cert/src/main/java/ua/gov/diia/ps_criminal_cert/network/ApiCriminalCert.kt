package ua.gov.diia.ps_criminal_cert.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.gov.diia.core.models.common_compose.general.DiiaResponse
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.ps_criminal_cert.models.request.AddressRequest
import ua.gov.diia.ps_criminal_cert.models.request.BirthPlaceRequest
import ua.gov.diia.ps_criminal_cert.models.request.BirthPlaceSelectionRequest
import ua.gov.diia.ps_criminal_cert.models.request.ContactsRequest
import ua.gov.diia.ps_criminal_cert.models.request.CriminalCertApplicationRequest
import ua.gov.diia.ps_criminal_cert.models.request.FormatExtractRequest
import ua.gov.diia.ps_criminal_cert.models.request.FullNamesBeforeRequest
import ua.gov.diia.ps_criminal_cert.models.request.NationalitiesRequest
import ua.gov.diia.ps_criminal_cert.models.request.RepeatedDeliveryConfirmationRequest
import ua.gov.diia.ps_criminal_cert.models.request.TypeDeliveryRequest
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmed

interface ApiCriminalCert {

    @Analytics("getCriminalCertsDetails")
    @GET("api/v2/public-service/criminal-cert/{applicationId}")
    suspend fun getCriminalCertsDetails(
        @Path("applicationId") id: String
    ): DiiaResponse

    @Analytics("getCriminalCertReasons")
    @GET("api/v2/public-service/criminal-cert/reasons")
    suspend fun getCriminalCertReasons(): DiiaResponse

    @Analytics("getCriminalCertTypes")
    @GET("api/v2/public-service/criminal-cert/types")
    suspend fun getCriminalCertTypes(): DiiaResponse

    @Analytics("getCriminalCertHome")
    @GET("api/v2/public-service/criminal-cert/application/info")
    suspend fun getCriminalCertHome(
        @Query("publicService") publicService: String?,
        @Query("newApplication") newApplication: Boolean
    ): DiiaResponse

    @Analytics("createCriminalCertApplication")
    @POST("api/v1/public-service/criminal-cert/application/create")
    suspend fun createCriminalCertApplication(
        @Body body: CriminalCertApplicationRequest
    ): CriminalCertConfirmed

    @Analytics("cancelCriminalCertApplication")
    @DELETE("api/v1/public-service/criminal-cert/{applicationId}")
    suspend fun cancelCriminalCertApplication(
        @Path("applicationId") applicationId: String,
        @Query("force") force: Boolean,
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertRequester")
    @GET("api/v2/public-service/criminal-cert/requester/{applicationId}")
    suspend fun getCriminalCertRequester(
        @Path("applicationId") applicationId: String
    ): DiiaResponse

    @Analytics("saveCriminalCertRequester")
    @PATCH("api/v1/public-service/criminal-cert/save-requester/{applicationId}")
    suspend fun saveCriminalCertRequester(
        @Path("applicationId") applicationId: String,
        @Body body: FullNamesBeforeRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertBirthPlace")
    @GET("api/v2/public-service/criminal-cert/birth-place/{applicationId}")
    suspend fun getCriminalCertBirthPlace(
        @Path("applicationId") applicationId: String,
        @Query("isDetails") isDetails: Boolean?
    ): DiiaResponse

    @Analytics("getNextStepBirthPlace")
    @PATCH("api/v1/public-service/criminal-cert/birth-place-get-next-step/{applicationId}")
    suspend fun getNextStepBirthPlace(
        @Path("applicationId") applicationId: String,
        @Body body: BirthPlaceSelectionRequest
    ): DiiaResponse

    @Analytics("saveCriminalCertBirthPlace")
    @PATCH("api/v1/public-service/criminal-cert/save-birth-place/{applicationId}")
    suspend fun saveCriminalCertBirthPlace(
        @Path("applicationId") applicationId: String,
        @Body body: BirthPlaceRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertNationalities")
    @GET("api/v2/public-service/criminal-cert/nationalities/{applicationId}")
    suspend fun getCriminalCertNationalities(
        @Path("applicationId") applicationId: String,
        @Query("citizenshipFirst") citizenshipFirst: String?
    ): DiiaResponse

    @Analytics("saveCriminalCertNationalities")
    @PATCH("api/v1/public-service/criminal-cert/save-nationalities/{applicationId}")
    suspend fun saveCriminalCertNationalities(
        @Path("applicationId") applicationId: String,
        @Body body: NationalitiesRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertFormatExtract")
    @GET("api/v1/public-service/criminal-cert/format-extract/{applicationId}")
    suspend fun getCriminalCertFormatExtract(
        @Path("applicationId") applicationId: String
    ): DiiaResponse

    @Analytics("saveCriminalCertFormatExtract")
    @PATCH("api/v1/public-service/criminal-cert/save-format-extract/{applicationId}")
    suspend fun saveCriminalCertFormatExtract(
        @Path("applicationId") applicationId: String,
        @Body body: FormatExtractRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertDeliveryType")
    @GET("api/v1/public-service/criminal-cert/type-delivery/{applicationId}")
    suspend fun getCriminalCertDeliveryType(
        @Path("applicationId") applicationId: String,
        @Query("repeatedDelivery") repeatedDelivery: Boolean?

    ): DiiaResponse

    @Analytics("saveCriminalCertDeliveryType")
    @PATCH("api/v1/public-service/criminal-cert/save-type-delivery/{applicationId}")
    suspend fun saveCriminalCertDeliveryType(
        @Path("applicationId") applicationId: String,
        @Body body: TypeDeliveryRequest
    ): CriminalCertConfirmed

    @Analytics("saveCriminalCertDeliveryAddress")
    @PATCH("api/v1/public-service/criminal-cert/save-delivery-address/{applicationId}")
    suspend fun saveCriminalCertDeliveryAddress(
        @Path("applicationId") applicationId: String,
        @Body body: AddressRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertContacts")
    @GET("api/v4/public-service/criminal-cert/contacts/{applicationId}")
    suspend fun getCriminalCertContacts(
        @Path("applicationId") applicationId: String,
        @Query("repeatedDelivery") repeatedDelivery: Boolean?
    ): DiiaResponse

    @Analytics("saveCriminalCertContacts")
    @PATCH("api/v1/public-service/criminal-cert/save-contacts/{applicationId}")
    suspend fun saveCriminalCertContacts(
        @Path("applicationId") applicationId: String,
        @Body body: ContactsRequest
    ): CriminalCertConfirmed

    @Analytics("getCriminalCertConfirmation")
    @POST("api/v2/public-service/criminal-cert/confirmation/{applicationId}")
    suspend fun getCriminalCertConfirmation(
        @Path("applicationId") applicationId: String
    ): DiiaResponse

    @Analytics("confirmApplication")
    @POST("api/v2/public-service/criminal-cert/application/send/{applicationId}")
    suspend fun confirmApplication(
        @Path("applicationId") applicationId: String
    ): CriminalCertConfirmed

    //repeated delivery

    @Analytics("getCrimeCertRepeatedDeliveryNextStep")
    @GET("api/v1/public-service/criminal-cert/repeated-delivery-get-next-step")
    suspend fun getCrimeCertRepeatedDeliveryNextStep(
        @Query("applicationId") applicationId: String,
        @Query("typeDelivery") typeDelivery: String?,
        @Query("addressId") addressId: String?,
        @Query("force") force: Boolean?
    ): CriminalCertConfirmed

    @Analytics("getCrimeCertRepeatedDeliveryConfirmation")
    @POST("api/v1/public-service/criminal-cert/repeated-delivery-confirmation")
    suspend fun getCrimeCertRepeatedDeliveryConfirmation(
        @Body body: RepeatedDeliveryConfirmationRequest
    ): DiiaResponse

    @Analytics("cancelCriminalCertRepeatedDelivery")
    @DELETE("api/v1/public-service/criminal-cert/cancel-repeated-delivery/{id}")
    suspend fun cancelCriminalCertRepeatedDelivery(
        @Path("id") id: String,
        @Query("force") force: Boolean,
    ): CriminalCertConfirmed

    @Analytics("createCrimeCertRepeatedDeliveryApplication")
    @POST("api/v1/public-service/criminal-cert/repeated-delivery-create")
    suspend fun createCrimeCertRepeatedDeliveryApplication(
        @Body body: RepeatedDeliveryConfirmationRequest
    ): DiiaResponse
}