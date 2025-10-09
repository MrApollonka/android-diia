package ua.gov.diia.faq.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootContainer
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.navigation.BaseNavigation

@AndroidEntryPoint
class FaqSearchF : Fragment() {

    private val viewModel: FaqSearchVM by viewModels()
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
            val navEvent by viewModel.navigationEvent.collectAsStateWithLifecycle(null)
            LaunchedEffect(navEvent) {
                navEvent?.let { path ->
                    when (path) {
                        is BaseNavigation.Back -> {
                            findNavController().navigateUp()
                        }
                    }
                }
            }

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