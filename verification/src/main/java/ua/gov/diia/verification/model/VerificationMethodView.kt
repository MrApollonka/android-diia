package ua.gov.diia.verification.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationMethodView(
    val code: String,
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
): Parcelable