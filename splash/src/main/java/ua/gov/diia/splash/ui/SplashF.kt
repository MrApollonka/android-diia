package ua.gov.diia.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import javax.inject.Inject

@AndroidEntryPoint
class SplashF : Fragment() {

    private var composeView: ComposeView? = null
    private val viewModel: SplashFVM by viewModels()

    @Inject
    lateinit var splashHelper: SplashHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is SplashFVM.Navigation.ToErrorDialog -> {
                            navigate(SplashFDirections.actionSplashFToRootDF(navigation.diiaError))
                        }

                        is SplashFVM.Navigation.ToPinCreation -> {
                            navigateToPinCreation()
                        }

                        is SplashFVM.Navigation.ToLogin -> {
                            navigate(SplashFDirections.actionSplashFToDestinationLogin())
                        }

                        is SplashFVM.Navigation.ToQrScanner -> {
                            splashHelper.navigateToQrScan(fragment = this@SplashF)
                        }

                        is SplashFVM.Navigation.ToProtection -> {
                            navigate(SplashFDirections.actionSplashFToDestinationPinInput())
                        }
                    }
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
            }

            SplashScreen()
        }

        registerForNavigationResultOnce(RESULT_KEY_PIN, viewModel::setServiceUserPin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToPinCreation() {
        splashHelper.navigateToProtectionCreation(
            host = this,
            resultDestinationId = currentDestinationId ?: return,
            resultKey = RESULT_KEY_PIN,
        )
    }

    private companion object {
        const val RESULT_KEY_PIN = "SplashF.RESULT_KEY_PIN"
    }

}