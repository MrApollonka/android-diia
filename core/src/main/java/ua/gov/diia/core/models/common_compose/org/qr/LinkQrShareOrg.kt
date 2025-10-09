package ua.gov.diia.core.models.common_compose.org.qr

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.text.PaginationMessageMlc
import ua.gov.diia.core.models.common_compose.org.chip.CenterChipBlackTabsOrg
import ua.gov.diia.core.models.common_compose.org.sharing.LinkSharingOrg

@Parcelize
@JsonClass(generateAdapter = true)
data class LinkQrShareOrg(
    @Json(name = "componentId")
    val componentId: String,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode?,
    @Json(name = "centerChipBlackTabsOrg")
    val centerChipBlackTabsOrg: CenterChipBlackTabsOrg?,
    @Json(name ="linkSharingOrg")
    val linkSharingOrg: LinkSharingOrg?,
    @Json(name = "qrCodeOrg")
    val qrCodeOrg: QrCodeOrg?,
    @Json(name = "paginationMessageMlc")
    val paginationMessageMlc: PaginationMessageMlc?
): Parcelable
