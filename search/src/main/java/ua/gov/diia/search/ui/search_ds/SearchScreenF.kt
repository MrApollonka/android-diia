package ua.gov.diia.search.ui.search_ds

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
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.search.models.SearchResult
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation

@AndroidEntryPoint
class SearchScreenF : Fragment() {

    private val viewModel: SearchScreenVM by viewModels()
    private val args: SearchScreenFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = selectDataList() ?: return
        val resultKey = arguments?.getString(ARGS_KEY) ?: return
        with(viewModel) {
            getScreenContent(
                navBarTitle = args.navBarTitle,
                contextMenu = args.contextMenu,
                searchPlaceholder = args.searchPlaceholder,
                data = data,
                resultKey = resultKey,
                requestCode = args.argRequestCode
            )
        }
    }

    private fun selectDataList(): List<SearchableItem>? {
        return arguments?.getParcelableArray(ARGS_ARRAY_ITEMS)?.mapNotNull { it as? SearchableItem }
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
                initial = Pair(UIActionKeysCompose.PAGE_LOADING_CIRCULAR, true)
            )
            val progressIndicator = viewModel.progressIndicator.collectAsState(initial = Pair("", true))

            viewModel.apply {

                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }

                        is BaseNavigation.ContextMenu -> {
                            navigateToContextMenu(this@SearchScreenF, navigation.contextMenuArray ?: emptyArray())
                        }

                        is SearchScreenVM.SearchScreenNavigation.NavBackWithResult -> {
                            sendResult(navigation.result)
                        }
                    }
                }
            }

            PublicServiceScreen(
                contentLoaded = contentLoaded.value,
                progressIndicator = progressIndicator.value,
                toolbar = topGroup,
                body = body,
                bottom = bottom,
                useGradientBg = false,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@SearchScreenF, "")
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@SearchScreenF)
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()
            }
        }
    }

    private fun sendResult(result: SearchResult) {
        if (result.item == null) return
        setNavigationResult(key = result.key, result = result.item)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private companion object {
        const val ARGS_KEY = "key"
        const val ARGS_ARRAY_ITEMS = "searchableList"
    }
}