package ua.gov.diia.core.util.extensions.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.core.util.state.start
import ua.gov.diia.core.util.state.stop

fun <T> T.executeAction(
    progressIndicator: MutableLiveData<Boolean>? = null,
    contentLoadedIndicator: MutableLiveData<Boolean>? = null,
    errorIndicator: MutableLiveData<Boolean>? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    templateKey: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    action: suspend CoroutineScope.() -> Unit
) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
    progressIndicator?.postValue(true)
    contentLoadedIndicator?.postValue(false)
    errorIndicator?.postValue(false)
    viewModelScope.launch(dispatcher) {
        try {
            action.invoke(this)
            resetErrorCounter()
        } catch (e: Exception) {
            setLastAction {
                executeAction(
                    progressIndicator,
                    contentLoadedIndicator,
                    errorIndicator,
                    dispatcher,
                    templateKey,
                    action
                )
            }
            consumeException(e, templateKey)
            errorIndicator?.postValue(true)
        } finally {
            progressIndicator?.postValue(false)
            contentLoadedIndicator?.postValue(true)
        }
    }
}

fun <T> T.executeActionOnFlow(
    loader: MutableStateFlow<Loader>? = null,
    progressIndicator: MutableStateFlow<Boolean>? = null,
    contentLoadedIndicator: MutableStateFlow<Boolean>? = null,
    errorIndicator: MutableStateFlow<Boolean>? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    templateKey: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    action: suspend CoroutineScope.() -> Unit
) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
    viewModelScope.launch(dispatcher) {
        try {
            withContext(Dispatchers.Main) {
                progressIndicator?.value = true
                contentLoadedIndicator?.value = false
                loader?.start()
                errorIndicator?.value = false
            }
            action.invoke(this)
            resetErrorCounter()
        } catch (e: Exception) {
            setLastAction {
                executeActionOnFlow(
                    loader,
                    progressIndicator,
                    contentLoadedIndicator,
                    errorIndicator,
                    dispatcher,
                    templateKey,
                    action
                )
            }
            consumeException(e, templateKey)
            withContext(Dispatchers.Main) {
                errorIndicator?.value = true
            }
        } finally {
            withContext(Dispatchers.Main) {
                progressIndicator?.value = false
                contentLoadedIndicator?.value = true
                loader?.stop()
            }
        }
    }
}