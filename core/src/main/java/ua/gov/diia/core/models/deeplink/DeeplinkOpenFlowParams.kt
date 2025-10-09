package ua.gov.diia.core.models.deeplink

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeeplinkOpenFlowParams(
    val flowId: String,
    val resourceId: String,
    val resourceSubtypeId: String?
) : Parcelable