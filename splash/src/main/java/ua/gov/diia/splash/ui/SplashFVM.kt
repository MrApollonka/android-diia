package ua.gov.diia.splash.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.DiiaError
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.util.work.WorkScheduler
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import javax.inject.Inject

@HiltViewModel
class SplashFVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @UnauthorizedClient private val apiAuth: ApiAuth,
    private val authorizationRepository: AuthorizationRepository,
    private val splashHelper: SplashHelper,
    private val workManager: WorkManager,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val crashlytics: WithCrashlytics,
    private val worksToSchedule: Set<@JvmSuppressWildcards WorkScheduler>,
) : ViewModel(), WithRetryLastAction by retryLastAction, WithErrorHandlingOnFlow by errorHandling {

    private val skipInitialization = savedStateHandle.get<Boolean>("skipInitialization") ?: false
    private val uuid4 = savedStateHandle.get<String>("uuid4")

    private val _navigation = Channel<Navigation>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    private var serviceUserUuid: String? = null
    private var serviceUserToken: String? = null

    init {
        viewModelScope.launch {
            serviceUserUuid = uuid4 ?: authorizationRepository.getServiceUserUUID()
            if (uuid4 != null) {
                startLoginServiceUserFlow()
            } else {
                if (!skipInitialization) {
                    setupAppVersionVerificationServices()
                }
            }

            if (serviceUserUuid != null) {
                resolveServiceUserNavigation()
            } else {
                when (authorizationRepository.getUserType()) {
                    UserType.PRIMARY_USER -> resolvePrimaryUserNavigation()
                    UserType.SERVICE_USER -> {
                        _navigation.send(Navigation.ToProtection)
                    }
                }
            }
        }
    }

    fun setServiceUserPin(pin: String) {
        val authToken = serviceUserToken ?: return
        val serviceUserUUID = serviceUserUuid ?: return
        executeActionOnFlow {
            authorizeServiceUser(pin, authToken, serviceUserUUID)
            _navigation.send(Navigation.ToQrScanner)
        }
    }

    private fun setupAppVersionVerificationServices() {
        viewModelScope.launch {
            scheduleWorkers()
        }
    }

    private fun startLoginServiceUserFlow() {
        viewModelScope.launch {
            loginAsServiceUser()
            setupAppVersionVerificationServices()
        }
    }

    private fun loginAsServiceUser() {
        executeActionOnFlow {
            val uuid = serviceUserUuid ?: return@executeActionOnFlow
            val mobileUuid = authorizationRepository.getMobileUuid()
            val tokenData = apiAuth.getServiceAccountToken(uuid, mobileUuid)

            if (tokenData.template == null) {
                serviceUserToken = tokenData.token
            } else {
                crashlytics.sendNonFatalError(IllegalStateException("Service user token is null"))
            }
        }
    }

    /**
     * Util function which encapsulates complex primary user navigation logic for readability
     * purpose
     */
    private fun resolvePrimaryUserNavigation() {
        executeActionOnFlow {
            if (splashHelper.isProtectionExists()) {
                _navigation.send(Navigation.ToProtection)
            } else {
                _navigation.send(Navigation.ToLogin)
            }
        }
    }

    private fun resolveServiceUserNavigation() {
        executeActionOnFlow {
            if (splashHelper.isProtectionExists()) {
                _navigation.send(Navigation.ToProtection)
            } else {
                _navigation.send(Navigation.ToPinCreation)
            }
        }
    }

    private suspend fun authorizeServiceUser(
        pin: String,
        authToken: String,
        serviceUserUuid: String
    ) {
        //splits execution to separate coroutines to speed up execution time
        coroutineScope {
            launch { authorizationRepository.setToken(authToken) }
            launch { authorizationRepository.setIsServiceUser(true) }
            launch { authorizationRepository.setServiceUserUUID(serviceUserUuid) }
            launch { splashHelper.setUserAuthorized(pin) }
        }
    }

    private fun scheduleWorkers() {
        worksToSchedule.forEach {
            it.enqueue(workManager)
        }
    }

    sealed class Navigation : NavigationPath {
        data class ToErrorDialog(val diiaError: DiiaError) : Navigation()
        data object ToLogin : Navigation()
        data object ToProtection : Navigation()
        data object ToQrScanner : Navigation()
        data object ToPinCreation : Navigation()
    }

}