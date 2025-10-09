package ua.gov.diia.faq.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.state.Loader
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootContainer
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.navigation.BaseNavigation

@AndroidEntryPoint
class FaqF : Fragment() {

    private val viewModel: FaqVM by viewModels()
    private var composeView: ComposeView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            viewModel.navigationEvent.collectAsEffect { path ->
                when (path) {
                    is BaseNavigation.Back -> {
                        findNavController().navigateUp()
                    }

                    is FaqVM.Navigation.NavigateToSearch -> {
                        findNavController().navigate(
                            FaqFDirections.navigateToSearch()
                        )
                    }

                    is FaqVM.Navigation.NavigateToSelf -> {
                        findNavController().navigate(
                            FaqFDirections.navigateToSelf(
                                categoryId = path.categoryId,
                                isRoot = false
                            )
                        )
                    }
                }
            }

            val loader by viewModel.loader.collectAsState(initial = Loader.create())

            val toolbar = viewModel.top

            val body = viewModel.body

            ComposeRootScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.bg_blue_yellow_gradient),
                        contentScale = ContentScale.FillBounds
                    )
                    .provideTestTagsAsResourceId(),
                loader = loader,
                toolbar = {
                    ToolbarRootContainer(
                        toolbarViews = toolbar,
                        onUIAction = viewModel::onUIAction
                    )
                },
                body = {
                    BodyRootLazyContainer(
                        bodyViews = body,
                        onUIAction = viewModel::onUIAction
                    )
                },
                onEvent = viewModel::onUIAction
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

}