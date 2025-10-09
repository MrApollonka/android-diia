package ua.gov.diia.core.models.common_compose.org.sharing

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconPlainGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.sharing.LinkSharingMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class LinkSharingOrg(
    @Json(name = "componentId")
    val componentId: String,
    @Json(name = "text")
    val text: String?,
    @Json(name = "linkSharingMlc")
    val linkSharingMlc: LinkSharingMlc,
    @Json(name = "description")
    val description: String?,
    @Json(name = "btnIconPlainGroupMlc")
    val btnIconPlainGroupMlc: BtnIconPlainGroupMlc?
) : Parcelable
