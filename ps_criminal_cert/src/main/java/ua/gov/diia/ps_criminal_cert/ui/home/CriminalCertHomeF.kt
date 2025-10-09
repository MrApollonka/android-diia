package ua.gov.diia.ps_criminal_cert.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_REFRESH_HOME
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_STATUS
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CriminalCertHomeF : Fragment() {

    private val viewModel: CriminalCertHomeVM by viewModels()
    private val args: CriminalCertHomeFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            args.navBarTitle?.run(::setNavBarTitle)
            args.previewItem?.let { handlePushNavigation(it) }
            getScreenContent(args.publicService, args.isNew)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val topGroup = viewModel.topGroupData
            val body = viewModel.bodyData
            val bottom = viewModel.bottomData

            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_CIRCULAR,
                    true
                )
            )
            val progressIndicator =
                viewModel.progressIndicator.collectAsState(initial = Pair("", true))

            viewModel.apply {

                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }

                        is BaseNavigation.ContextMenu -> {
                            navigateToContextMenu(
                                this@CriminalCertHomeF,
                                navigation.contextMenuArray ?: emptyArray()
                            )
                        }

                        is CriminalCertHomeVM.HomeNavigation.ToReasons -> {
                            navigateToReasons()
                        }

                        is CriminalCertHomeVM.HomeNavigation.ToStatus -> {
                            navigation.id?.let { navigateToStatus(it) }
                        }

                        is CriminalCertHomeVM.HomeNavigation.ToRequester -> {
                            navigation.id?.let { navigateToRequester(it) }
                        }
                    }
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
                showRatingDialogByUserInitiative.collectAsEffect {
                    val ratingModel = it.peekContent()
                    val formCode: String? = ratingModel.formCode
                    navigateToRatingService(
                        fragment = this@CriminalCertHomeF,
                        ratingFormModel = ratingModel,
                        id = null,
                        destinationId = R.id.criminalCertHomeF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_HOME,
                        ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                        formCode = formCode
                    )
                }
            }

            PublicServiceScreen(
                contentLoaded = contentLoaded.value,
                progressIndicator = progressIndicator.value,
                toolbar = topGroup,
                body = body,
                bottom = bottom,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(
                    this@CriminalCertHomeF,
                    CriminalCertConst.FEATURE_CODE
                )

                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertHomeF)
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES -> findNavController().popBackStack()
                ActionsConst.RATING -> viewModel.getRatingForm()
                ACTION_CODE_STATUS -> viewModel.applicationId?.let { navigateToStatus(it) }
            }
        }

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
        registerForNavigationResultOnce<String>(CriminalCertConst.ACTION_CODE_PUBLIC_SERVICES) { action ->
            if (action == ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES) findNavController().popBackStack()
        }
        registerForNavigationResultOnce<String>(ACTION_CODE_REFRESH_HOME) { action ->
            if (action == ActionsConst.ACTION_FORCE_UPDATE || action == ActionsConst.DIALOG_ACTION_REFRESH) {
                val isNewApp = if (action == ActionsConst.ACTION_FORCE_UPDATE) true else args.isNew
                viewModel.clearContent()
                viewModel.getScreenContent(args.publicService, isNewApp)
            }
        }
    }

    private fun navigateToReasons() {
        navigate(
            CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertStepReasonsF(
                args.contextMenu,
                args.navBarTitle
            )
        )
    }

    private fun handlePushNavigation(consumablePushItem: ConsumableItem?) {
        if (consumablePushItem != null) {
            if (!consumablePushItem.isConsumed && consumablePushItem.item is PullNotificationItemSelection) {
                consumablePushItem.isConsumed = true
                val id =
                    (consumablePushItem.item as PullNotificationItemSelection).resourceId ?: return
                navigateToStatus(id)
            }
        }
    }

    private fun navigateToStatus(id: String) {
        navigate(
            CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertDetailsF(
                contextMenu = args.contextMenu,
                certId = id
            )
        )
    }

    private fun navigateToRequester(id: String) {
        navigate(
            CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertStepRequesterF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}