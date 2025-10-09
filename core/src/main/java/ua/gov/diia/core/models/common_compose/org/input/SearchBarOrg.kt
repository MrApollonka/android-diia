package ua.gov.diia.core.models.common_compose.org.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnWhiteAdditionalIconAtm
import ua.gov.diia.core.models.common_compose.general.PaddingMode
import ua.gov.diia.core.models.common_compose.mlc.input.SearchInputMlc

@JsonClass(generateAdapter = true)
data class SearchBarOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "searchInputMlc")
    val searchInputMlc: SearchInputMlc,
    @Json(name = "btnWhiteAdditionalIconAtm")
    val btnWhiteAdditionalIconAtm: BtnWhiteAdditionalIconAtm? = null,
    @Json(name = "paddingMode")
    val paddingMode: PaddingMode? = null
)