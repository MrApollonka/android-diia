package ua.gov.diia.core.models.common_compose.org.qr

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.scancode.QrCodeMlc
import ua.gov.diia.core.models.common_compose.mlc.text.PaginationMessageMlc
import ua.gov.diia.core.models.common_compose.subatomic.ExpireLabel

@Parcelize
@JsonClass(generateAdapter = true)
data class QrCodeOrg(
    @Json(name = "componentId")
    val componentId: String,
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "qrCodeMlc")
    val qrCodeMlc: QrCodeMlc,
    @Json(name = "expireLabel")
    val expireLabel: ExpireLabel?,
    @Json(name = "stateAfterExpiration")
    val stateAfterExpiration: StateAfterExpiration?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class StateAfterExpiration(
    @Json(name = "paginationMessageMlc")
    val paginationMessageMlc: PaginationMessageMlc
) : Parcelable
