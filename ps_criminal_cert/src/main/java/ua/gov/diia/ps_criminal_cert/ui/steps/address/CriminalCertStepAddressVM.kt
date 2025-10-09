package ua.gov.diia.ps_criminal_cert.ui.steps.address

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.address_search.models.AddressFieldResponse
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.address_search.ui.AddressParameterMapper
import ua.gov.diia.address_search.ui.AddressSearchVM
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertApplicationInfoNextStep
import ua.gov.diia.ps_criminal_cert.models.enums.RepeatedDeliveryNextStep
import ua.gov.diia.ps_criminal_cert.models.request.AddressRequest
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ADDRESS_FEATURE_CODE
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepAddressVM @Inject constructor(
    @AuthorizedClient private val apiAddressSearch: ApiAddressSearch,
    @AuthorizedClient private val api: ApiCriminalCert,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    errorHandlingDelegate: WithErrorHandling,
    retryActionDelegate: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
    addressParameterMapper: AddressParameterMapper,
) : AddressSearchVM(
    apiAddressSearch,
    addressParameterMapper,
    errorHandlingDelegate,
    retryActionDelegate
),
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _isActionButtonLoading = MutableLiveData<Boolean>()
    val isActionButtonLoading = _isActionButtonLoading.asLiveData()

    private val _screenContentHasBeenLoaded = MutableLiveData<Boolean>()
    val screenContentHasBeenLoaded = _screenContentHasBeenLoaded.asLiveData()

    private val _navigateToContacts = MutableLiveData<UiDataEvent<String?>>()
    val navigateToContacts = _navigateToContacts.asLiveData()

    private val _screenContent = MutableLiveData<AddressFieldResponse>()
    val screenContent = _screenContent.asLiveData()

    private var applicationId: String? = null
    private var navBarTitle: String? = null
    var addressId: String? = null


    fun doInit(
        contextMenu: Array<ContextMenuField>?,
        applicationId: String,
        navBarTitle: String?,
    ) {
        setContextMenu(contextMenu)
        this.applicationId = applicationId
        this.navBarTitle = navBarTitle
    }

    fun loadScreenContent(addressScheme: String) {
        executeAction(
            progressIndicator = _isLoading,
            contentLoadedIndicator = _screenContentHasBeenLoaded
        ) {
            val data = apiAddressSearch.getFieldContext(
                featureCode = ADDRESS_FEATURE_CODE,
                addressTemplateCode = addressScheme
            )

            data.template?.let {
                showTemplateDialog(it)
            } ?: run {
                _screenContent.value = data
                setAddressSearchArs(data, ADDRESS_FEATURE_CODE, addressScheme)
            }
        }
    }

    fun saveDeliveryAddress(addressId: String) {
        executeAction(
            progressIndicator = _isActionButtonLoading
        ) {
            val request = AddressRequest(
                addressId = addressId,
            )
            applicationId?.let { id ->
                api.saveCriminalCertDeliveryAddress(id, request).let { response ->
                    response.template?.let { showTemplateDialog(it) }
                    when (response.nextStep) {
                        CriminalCertApplicationInfoNextStep.CONTACTS.code -> {
                            _navigateToContacts.value = UiDataEvent(addressId)
                        }
                    }
                }
            }
        }
    }

    fun refreshAddressScheme(addressScheme: String) {
        cleanUpCountryGroup()
        loadScreenContent(addressScheme)
    }

    fun getRepeatedDeliveryNextStep(addressId: String, force: Boolean? = null) {
        executeAction(
            progressIndicator = _isActionButtonLoading
        ) {
            if (!applicationId.isNullOrEmpty()) {
                api.getCrimeCertRepeatedDeliveryNextStep(
                    applicationId = applicationId ?: return@executeAction,
                    force = force,
                    addressId = addressId,
                    typeDelivery = null
                ).let {
                    it.template?.let {
                        showTemplateDialog(it)
                    }
                    it.nextStep?.let {
                        when (it) {

                            RepeatedDeliveryNextStep.CONTACTS_REPEATED_DELIVERY.code -> {
                                _navigateToContacts.value = UiDataEvent(addressId)
                            }

                            else -> {
                                //noting
                            }
                        }
                    }
                }
            }
        }
    }


    fun getRatingForm() {
        getRating(
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(
            ratingRequest = ratingRequest,
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }
}