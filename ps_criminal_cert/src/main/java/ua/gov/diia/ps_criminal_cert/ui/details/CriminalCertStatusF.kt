package ua.gov.diia.ps_criminal_cert.ui.details

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
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.openPdfFile
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.core.util.extensions.fragment.shareLink
import ua.gov.diia.payment.PaymentConst
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_CANCEL_APPLICATION_WITH_FORCE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_CANCEL_RD_WITH_FORCE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_REFRESH_HOME
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_CODE_STATUS
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.ACTION_PUBLIC_SERVICES
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.DIALOG_ACTION_CANCEL_APPLICATION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.FEATURE_CODE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import javax.inject.Inject

@AndroidEntryPoint
class CriminalCertStatusF : Fragment() {

    private val viewModel: CriminalCertStatusVM by viewModels()
    private val args: CriminalCertStatusFArgs by navArgs()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var withCrashlytics: WithCrashlytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            getScreenContent(args.certId)
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
                paymentDataJson.observeUiDataEvent(viewLifecycleOwner, ::startPaymentFlow)

                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            navigateHome()
                        }

                        is BaseNavigation.ContextMenu -> {
                            navigateToContextMenu(
                                this@CriminalCertStatusF,
                                navigation.contextMenuArray ?: emptyArray()
                            )
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToRequester -> {
                            navigation.id?.let { navigateToRequester(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToNationalities -> {
                            navigation.id?.let { navigateToNationalities(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToFormatExtract -> {
                            navigation.id?.let { navigateToFormatExtract(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToBirthPlace -> {
                            navigation.id?.let { navigateToBirthPlace(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToDeliveryType -> {
                            navigation.id?.let {
                                navigateToDeliveryType(
                                    it,
                                    navigation.repeatedDelivery
                                )
                            }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToContacts -> {
                            navigation.id?.let {
                                navigateToContacts(
                                    it,
                                    navigation.repeatedDelivery
                                )
                            }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToConfirmation -> {
                            navigation.id?.let { navigateToConfirmation(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToDeliveryAddress -> {
                            navigation.id?.let { navigateToDeliveryAddress(it, navigation.scheme) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToHomeScreen -> {
                            navigateHome(forceUpdate = navigation.newApplication)
                        }

                        is CriminalCertStatusVM.StatusNavigation.ShareLink -> {
                            navigation.link?.let { shareLink(it) }
                        }

                        is CriminalCertStatusVM.StatusNavigation.ToOpenLink -> {
                            navigation.url?.let { openLink(it, withCrashlytics) }
                        }
                    }
                }

                openDocument.collectAsEffect {
                    if (it.peekContent().isNotEmpty()) {
                        openPdfFile(it.peekContent(), withCrashlytics)
                    }
                }


                shareCert.collectAsEffect {
                    sendPdf(this@CriminalCertStatusF, it.file, it.name, withCrashlytics)
                }

                downloadCert.collectAsEffect {
                    sendZip(this@CriminalCertStatusF, it.file, it.name, withCrashlytics)
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }

                showRatingDialogByUserInitiative.collectAsEffect {
                    val ratingModel = it.peekContent()
                    val formCode: String? = ratingModel.formCode
                    navigateToRatingService(
                        fragment = this@CriminalCertStatusF,
                        ratingFormModel = ratingModel,
                        id = args.certId,
                        destinationId = R.id.criminalCertDetailsF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_STATUS,
                        ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                        formCode = formCode
                    )
                }

                showRatingDialog.collectAsEffect {
                    val ratingModel = it.peekContent()
                    val formCode: String? = ratingModel.formCode
                    navigateToRatingService(
                        fragment = this@CriminalCertStatusF,
                        ratingFormModel = ratingModel,
                        id = args.certId,
                        destinationId = R.id.criminalCertDetailsF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_STATUS,
                        ratingType = ActionsConst.RATING_TYPE_REQUESTED,
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
                }
            )
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()

                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(
                    this@CriminalCertStatusF,
                    FEATURE_CODE
                )

                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStatusF)

                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()

                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()

                ActionsConst.RATING -> viewModel.getRatingForm()
                DIALOG_ACTION_CANCEL_APPLICATION -> viewModel.cancelApplication(true)
                ActionsConst.DIALOG_ACTION_PUBLIC_SERVICES -> navigateToPublicServices()
                ActionsConst.DIALOG_ACTION_REFRESH -> viewModel.refreshData()
                PaymentConst.ACTION_PAYMENT_COMPLETED -> viewModel.refreshData()
                ACTION_CODE_STATUS -> viewModel.refreshData()
                ACTION_CODE_CANCEL_APPLICATION_WITH_FORCE -> viewModel.cancelApplication(true)
                ACTION_CODE_CANCEL_RD_WITH_FORCE -> viewModel.cancelRepeatedDeliveryApplication(true)
                ACTION_PUBLIC_SERVICES -> navigateToPublicServices()
            }
        }

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }

        doOnSystemBackPressed(::navigateHome)
    }

    private fun navigateToRequester(id: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepRequesterF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    private fun navigateToNationalities(id: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepNationalityF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    private fun startPaymentFlow(json: String) {
        viewModel.startPaymentFlow(requireContext(), json) { resultCode ->
            viewModel.sendResultCode(resultCode)
        }
    }

    private fun navigateToFormatExtract(id: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertFormatExtractF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    private fun navigateToDeliveryAddress(id: String, scheme: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepAddressF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id,
                addressScheme = scheme
            )
        )
    }

    private fun navigateToBirthPlace(id: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepBirthF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
            )
        )
    }

    private fun navigateToContacts(id: String, repeatedDelivery: Boolean) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id,
                repeatedDelivery = repeatedDelivery
            )
        )
    }

    private fun navigateToDeliveryType(id: String, repeatedDelivery: Boolean) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertDeliveryTypeF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id,
                repeatedDelivery = repeatedDelivery
            )
        )
    }

    private fun navigateToConfirmation(id: String) {
        navigate(
            CriminalCertStatusFDirections.actionCriminalCertDetailsFToCriminalCertStepConfirmF(
                contextMenu = args.contextMenu,
                navBarTitle = args.navBarTitle,
                applicationId = id
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

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateHome(forceUpdate: Boolean = false) {
        setNavigationResult(
            arbitraryDestination = R.id.criminalCertHomeF,
            key = ACTION_CODE_REFRESH_HOME,
            data = if (forceUpdate) ActionsConst.ACTION_FORCE_UPDATE else ActionsConst.DIALOG_ACTION_REFRESH
        )
        findNavController().popBackStack(R.id.criminalCertHomeF, false)
    }
}