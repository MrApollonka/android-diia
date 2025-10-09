package ua.gov.diia.ui_base.models.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.select_location.Coordinate

@Parcelize
data class LocationWithStatus(
    val status: Status,
    val coordinate: Coordinate?,
    val accuracy: Double? = null,
) : Parcelable {

    @Parcelize
    enum class Status : Parcelable {
        VALID, MOCK, NOT_AVAILABLE
    }
}