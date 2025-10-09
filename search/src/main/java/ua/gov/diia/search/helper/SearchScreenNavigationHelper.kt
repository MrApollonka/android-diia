package ua.gov.diia.search.helper

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingFormModel

interface SearchScreenNavigationHelper {

    /**
     * Navigate to ContextMenu
     */
    fun navigateToContextMenu(fragment: Fragment, menu: Array<ContextMenuField>)

    /**
     * Navigate to FAQ screen
     */
    fun navigateToFaq(fragment: Fragment, categoryId: String)

    /**
     * Navigate to Rating Form
     */
    fun navigateToRatingService(
        fragment: Fragment, ratingFormModel: RatingFormModel,
        id: String?,
        @IdRes destinationId: Int,
        resultKey: String,
        screenCode: String? = null,
        ratingType: String? = null,
        formCode: String? = null
    )

    /**
     * Navigate to Support screen
     */
    fun navigateToSupport(fragment: Fragment)

    /**
     * performs navigation to ps by key
     */
    fun navigateToGlobalDestinationPS(fragment: Fragment, currentDestinationId: Int, resultKey: String, psKey: String)
}