package ua.gov.diia.opensource.util.delegation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import javax.inject.Inject

class DefaultRatingDialogBehaviourOnFlow @Inject constructor() : WithRatingDialogOnFlow {

    override val showRatingDialog =
        mutableSharedFlowOf<UiDataEvent<RatingFormModel>>()

    override val showRatingDialogByUserInitiative =
        mutableSharedFlowOf<UiDataEvent<RatingFormModel>>()

    override val sendingRatingResult =
        MutableStateFlow(false)

    override fun showRatingDialog(
        ratingDialog: RatingFormModel,
        key: String
    ) {
        /* no-op */
    }

    override fun <T> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String,
        resourceId: String?
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
        /* no-op */
    }

    override fun <T : ViewModel> T.getRating(
        category: String,
        serviceCode: String,
        resourceId: String?,
        screenCode: String?
    ) where T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
        /* no-op */
    }

}