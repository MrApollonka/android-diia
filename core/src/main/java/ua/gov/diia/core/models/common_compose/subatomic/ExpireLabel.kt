package ua.gov.diia.core.models.common_compose.subatomic

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.TextParameter

@Parcelize
@JsonClass(generateAdapter = true)
data class ExpireLabel(
    @Json(name = "expireLabelFirst")
    val expireLabelFirst: String,
    @Json(name = "expireLabelLast")
    val expireLabelLast: String?,
    @Json(name = "timer")
    val timer: Int,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?
) : Parcelable