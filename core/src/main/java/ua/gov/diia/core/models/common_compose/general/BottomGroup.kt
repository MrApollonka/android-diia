package ua.gov.diia.core.models.common_compose.general

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.mlc.button.BtnSlideMlc
import ua.gov.diia.core.models.common_compose.org.bottom.BottomGroupOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg

@JsonClass(generateAdapter = true)
data class BottomGroup(
    @Json(name = "listItemGroupOrg")
    val listItemGroupOrg: ListItemGroupOrg?,
    @Json(name = "bottomGroupOrg")
    val bottomGroupOrg: BottomGroupOrg?,
    @Json(name = "checkboxBtnOrg")
    val checkboxBtnOrg: CheckboxBtnOrg? = null,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm?,
    @Json(name = "btnPrimaryDefaultAtm")
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtm? = null,
    @Json(name = "btnPlainAtm")
    val btnPlainAtm: BtnPlainAtm? = null,
    @Json(name = "btnSlideMlc")
    val btnSlideMlc: BtnSlideMlc?,
)