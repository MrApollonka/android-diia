package ua.gov.diia.core.util.delegation

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import ua.gov.diia.core.util.event.UiDataEvent

interface WithEmailShare<Request>: DefaultLifecycleObserver {

    val sharingEmail: LiveData<Boolean>

    val emailShareResult: LiveData<UiDataEvent<Boolean>>

    fun <T : Fragment> T.shareEmail(request: Request)
}