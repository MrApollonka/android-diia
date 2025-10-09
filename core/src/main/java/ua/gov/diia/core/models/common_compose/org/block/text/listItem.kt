package ua.gov.diia.core.models.common_compose.org.block.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class ListItem(
    @Json(name = "tableItemVerticalMlc")
    val tableItemVerticalMlc: TableItemVerticalMlc? = null,
    @Json(name = "tableItemHorizontalMlc")
    val tableItemHorizontalMlc: TableItemHorizontalMlc? = null
): Parcelable