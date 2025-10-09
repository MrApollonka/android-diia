package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.search.helper.SearchScreenNavigationHelper

class SearchScreenNavigationHelperImpl : SearchScreenNavigationHelper {

    override fun navigateToContextMenu(
        fragment: Fragment,
        menu: Array<ContextMenuField>
    ) {
        /* no-op */
    }

    override fun navigateToFaq(fragment: Fragment, categoryId: String) {
        /* no-op */
    }

    override fun navigateToRatingService(
        fragment: Fragment,
        ratingFormModel: RatingFormModel,
        id: String?,
        destinationId: Int,
        resultKey: String,
        screenCode: String?,
        ratingType: String?,
        formCode: String?
    ) {
        /* no-op */
    }

    override fun navigateToSupport(fragment: Fragment) {
        /* no-op */
    }

    override fun navigateToGlobalDestinationPS(
        fragment: Fragment,
        currentDestinationId: Int,
        resultKey: String,
        psKey: String
    ) {
        /* no-op */
    }

}