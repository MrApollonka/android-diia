package ua.gov.diia.verification.ui.method_selection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.organism.ContextMenuOrg
import ua.gov.diia.ui_base.fragments.BaseBottomDialog
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.verification.model.VerificationFlowResult
import ua.gov.diia.verification.model.VerificationMethodView

@AndroidEntryPoint
internal class VerificationMethodSelectionDF : BaseBottomDialog() {

    private val args: VerificationMethodSelectionDFArgs by navArgs()
    private val viewModel: VerificationMethodSelectionVM by viewModels()
    private var composeView: ComposeView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        composeView?.setContent {
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        findNavController().popBackStack()
                    }

                    is VerificationMethodSelectionVM.Navigation.SelectedVerificationMethod -> {
                        sendResult(navigation.code)
                    }
                }
            }

            val data by viewModel.contextMenuOrgData.collectAsStateWithLifecycle()

            data?.let { lData ->
                ContextMenuOrg(
                    modifier = Modifier,
                    data = lData,
                    onUIAction = viewModel::onUIAction
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun sendResult(code: String) {
        setNavigationResult(
            arbitraryDestination = args.resultDestinationId,
            key = args.resultKey,
            data = ConsumableItem(VerificationFlowResult.VerificationMethod(code))
        )
        findNavController().popBackStack()
    }

}