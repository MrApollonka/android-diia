package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

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
import ua.gov.diia.address_search.models.NationalityItem
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
class CriminalCertStepNationalityF : Fragment() {

    private val viewModel: CriminalCertStepNationalityVM by viewModels()
    private val args: CriminalCertStepNationalityFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            doInit(args.navBarTitle)
            getScreenContent(args.applicationId, null)
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
                                this@CriminalCertStepNationalityF,
                                navigation.contextMenuArray ?: emptyArray()
                            )
                        }

                        is CriminalCertStepNationalityVM.NationalityNavigation.NavigateToNationalitySelection -> {
                            navigate(
                                CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToSearchF(
                                    key = CriminalCertConst.RESULT_KEY_NATIONALITIES,
                                    searchableList = navigation.nationalities.toTypedArray()
                                )
                            )
                        }

                        is CriminalCertStepNationalityVM.NationalityNavigation.NavigateToFormatExtract -> {
                            navigation.applicationId?.let { navigateToFormatExtract(it) }
                        }
                        is CriminalCertStepNationalityVM.NationalityNavigation.NavigateToContacts -> {
                            navigation.applicationId?.let { navigateToContacts(it) }
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
                        fragment = this@CriminalCertStepNationalityF,
                        ratingFormModel = ratingModel,
                        id = args.applicationId,
                        destinationId = R.id.criminalCertStepNationalityF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_NATIONALITY,
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
                    this@CriminalCertStepNationalityF,
                    CriminalCertConst.FEATURE_CODE
                )

                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepNationalityF)
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()
                ActionsConst.RATING -> viewModel.getRatingForm()
                ACTION_CODE_STATUS -> viewModel.applicationId?.let { navigateToStatus(it) }
                DIALOG_ACTION_CANCEL_APPLICATION -> viewModel.cancelApplication(true)
                ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES -> navigateToPublicServices()
            }
        }

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
        registerForNavigationResult<NationalityItem>(CriminalCertConst.RESULT_KEY_NATIONALITIES) { result ->
            viewModel.clearContent()
            viewModel.getScreenContent(
                id = args.applicationId,
                countryCode = result.id
            )
            viewModel.selectNationalityItem(result.id, result.label)
        }
    }

    private fun navigateToFormatExtract(applicationId: String) {
        navigate(
            CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertFormatExtractF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = applicationId
            )
        )
    }

    private fun navigateToContacts(applicationId: String) {
        navigate(
            CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = applicationId
            )
        )
    }

    private fun navigateToPublicServices() {
        setNavigationResult(
            arbitraryDestination = R.id.criminalCertHomeF,
            key = CriminalCertConst.ACTION_CODE_PUBLIC_SERVICES,
            data = ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES
        )
        findNavController().popBackStack(R.id.criminalCertHomeF, false)
    }

    private fun navigateToStatus(applicationId: String) {
        navigate(
            CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertDetailsF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                certId = applicationId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}