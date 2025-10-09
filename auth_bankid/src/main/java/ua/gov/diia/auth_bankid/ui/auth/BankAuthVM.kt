package ua.gov.diia.auth_bankid.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.HttpUrl.Companion.toHttpUrl
import ua.gov.diia.auth_bankid.model.BankAuthRequest
import ua.gov.diia.auth_bankid.ui.auth.BankAuthConst.CODE
import ua.gov.diia.auth_bankid.ui.auth.BankAuthConst.PROGRESS_ACTIVE
import ua.gov.diia.auth_bankid.ui.auth.BankAuthConst.PROGRESS_INACTIVE
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.verification.model.VerificationFlowResult
import javax.inject.Inject

@HiltViewModel
internal class BankAuthVM @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bankAuthRequest = savedStateHandle.get<BankAuthRequest>("requestData")

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    private val _uiData = mutableStateOf(BankAuthScreenData(progressLoadState = true))
    val uiData: State<BankAuthScreenData> = _uiData

    init {
        bankAuthRequest?.authUrl?.let { lAuthUrl ->
            _uiData.value = _uiData.value.copy(
                authUrl = lAuthUrl
            )
        }
    }

    fun parseAuthCode(callbackUrl: String) {
        callbackUrl.toHttpUrl().queryParameter(CODE)?.let { requestId ->
            val request = VerificationFlowResult.CompleteVerificationStep(
                requestId = requestId,
                bankCode = bankAuthRequest?.bankCode
            )
            _navigation.tryEmit(Navigation.CompleteAuth(request))
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            PROGRESS_ACTIVE -> {
                _uiData.value = _uiData.value.copy(progressLoadState = true)
            }

            PROGRESS_INACTIVE -> {
                _uiData.value = _uiData.value.copy(progressLoadState = false)
            }
        }
    }

    sealed class Navigation : NavigationPath {
        data class CompleteAuth(val data: VerificationFlowResult) : Navigation()
    }

}