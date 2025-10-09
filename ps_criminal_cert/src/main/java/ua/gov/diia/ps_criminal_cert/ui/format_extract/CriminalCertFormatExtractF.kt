package ua.gov.diia.ps_criminal_cert.ui.format_extract

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
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_STATUS
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.DIALOG_ACTION_CANCEL_APPLICATION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CriminalCertFormatExtractF : Fragment() {

    private val viewModel: CriminalCertFormatExtractVM by viewModels()
    private val args: CriminalCertFormatExtractFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            args.navBarTitle?.run(::setNavBarTitle)
            getScreenContent(args.applicationId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        doOnSystemBackPressed { navigateToStatus(args.applicationId) }
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
                            navigateToStatus(args.applicationId)
                        }

                        is BaseNavigation.ContextMenu -> {
                            navigateToContextMenu(
                                this@CriminalCertFormatExtractF,
                                navigation.contextMenuArray ?: emptyArray()
                            )
                        }

                        is CriminalCertFormatExtractVM.FormatExtractNavigation.ToDeliveryType -> {
                            navigation.id?.let { navigateToDeliveryType(it) }
                        }

                        is CriminalCertFormatExtractVM.FormatExtractNavigation.ToContacts -> {
                            navigation.id?.let { navigateContacts(it) }
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
                        fragment = this@CriminalCertFormatExtractF,
                        ratingFormModel = ratingModel,
                        id = args.applicationId,
                        destinationId = R.id.criminalCertFormatExtractF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_FORMAT_EXTRACT,
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
                    this@CriminalCertFormatExtractF,
                    CriminalCertConst.FEATURE_CODE
                )

                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertFormatExtractF)
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()
                ActionsConst.RATING -> viewModel.getRatingForm()
                DIALOG_ACTION_CANCEL_APPLICATION -> viewModel.cancelApplication(true)
                ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES -> navigateToPublicServices()
                ACTION_CODE_STATUS -> navigateToStatus(args.applicationId)
            }
        }

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
    }

    private fun navigateToPublicServices() {
        setNavigationResult(
            arbitraryDestination = R.id.criminalCertHomeF,
            key = CriminalCertConst.ACTION_CODE_PUBLIC_SERVICES,
            data = ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES
        )
        findNavController().popBackStack(R.id.criminalCertHomeF, false)
    }

    private fun navigateToDeliveryType(id: String) {
        navigate(
            CriminalCertFormatExtractFDirections.actionCriminalCertFormatExtractFToCriminalCertDeliveryTypeF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    private fun navigateToStatus(id: String) {
        navigate(
            CriminalCertFormatExtractFDirections.actionCriminalCertFormatExtractFToCriminalCertDetailsF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                certId = id
            )
        )
    }

    private fun navigateContacts(id: String) {
        navigate(
            CriminalCertFormatExtractFDirections.actionCriminalCertFormatExtractFToCriminalCertStepContactsF(
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