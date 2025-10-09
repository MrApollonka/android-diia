package ua.gov.diia.ui_base.fragments.dynamicdialog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.DIALOG_ACTION_CODE_LOGOUT
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryLargeAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeLargeAtmData
import ua.gov.diia.ui_base.components.atom.icon.MediumIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateDialogScreenData
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateDialogScreenData.Alignment
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateMlcV2Data
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_ALERT_REVIEW
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_HORIZONTAL_BUTTON
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_HORIZONTAL_BUTTONS
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_LARGE
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_LARGE_MIDDLE
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_LEFT_MIDDLE
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_MIDDLE_CENTER_BLACK_BUTTON
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_TYPE_SMALL
import ua.gov.diia.ui_base.fragments.dynamicdialog.TemplateDialogConst.ALERT_V2
import javax.inject.Inject

@HiltViewModel
class TemplateDialogVM @Inject constructor(
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>
) : ViewModel() {

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    private val _templateMlcV1Data = mutableStateOf<TemplateDialogScreenData?>(null)
    val templateMlcV1Data: State<TemplateDialogScreenData?> = _templateMlcV1Data

    private val _templateMlcV2Data = mutableStateOf<TemplateMlcV2Data?>(null)
    val templateMlcV2Data: State<TemplateMlcV2Data?> = _templateMlcV2Data

    fun doInit(dialog: TemplateDialogModel) {
        if (dialog.type == ALERT_V2) {
            _templateMlcV2Data.value = TemplateMlcV2Data(
                mediumIconAtm = dialog.data.icon?.let { iconCode ->
                    MediumIconAtmData(
                        code = iconCode
                    )
                },
                title = UiText.DynamicString(dialog.data.title),
                description = dialog.data.description?.let { lDescription ->
                    UiText.DynamicString(lDescription)
                },
                btnPrimaryLargeAtmData = BtnPrimaryLargeAtmData(
                    id = dialog.data.mainButton.action.orEmpty(),
                    title = UiText.DynamicString(dialog.data.mainButton.name.orEmpty()),
                    interactionState = UIState.Interaction.Enabled
                ),
                btnStrokeLargeAtmData = dialog.data.alternativeButton?.let { lAlternativeButton ->
                    BtnStrokeLargeAtmData(
                        id = lAlternativeButton.action.orEmpty(),
                        title = UiText.DynamicString(lAlternativeButton.name.orEmpty()),
                        interactionState = UIState.Interaction.Enabled
                    )
                }
            )
        } else {
            val alignment = when (dialog.type) {
                ALERT_TYPE_LARGE,
                ALERT_TYPE_LEFT_MIDDLE -> Alignment.LEFT

                ALERT_TYPE_SMALL,
                ALERT_TYPE_ALERT_REVIEW,
                ALERT_TYPE_HORIZONTAL_BUTTONS,
                ALERT_TYPE_HORIZONTAL_BUTTON,
                ALERT_TYPE_LARGE_MIDDLE -> Alignment.CENTER

                else -> Alignment.CENTER
            }

            var primaryBtn: BtnPrimaryDefaultAtmData? = null
            var secondaryBtn: BtnPlainAtmData? = null
            var strokeBtn: BtnStrokeLargeAtmData? = null

            when (dialog.type) {
                ALERT_TYPE_LARGE_MIDDLE -> {
                    strokeBtn = strokeBtn(
                        name = dialog.data.mainButton.name,
                        id = dialog.data.mainButton.action
                    )
                    secondaryBtn = secondaryBtn(
                        name = dialog.data.alternativeButton?.name,
                        id = dialog.data.alternativeButton?.action
                    )
                }

                ALERT_TYPE_MIDDLE_CENTER_BLACK_BUTTON -> {
                    primaryBtn = primaryBtn(
                        name = dialog.data.mainButton.name,
                        id = dialog.data.mainButton.action
                    )
                    secondaryBtn = secondaryBtn(
                        name = dialog.data.alternativeButton?.name,
                        id = dialog.data.alternativeButton?.action
                    )
                }

                ALERT_TYPE_SMALL -> {
                    strokeBtn = strokeBtn(
                        name = dialog.data.mainButton.name,
                        id = dialog.data.mainButton.action
                    )
                }

                ALERT_TYPE_LARGE -> {
                    strokeBtn = strokeBtn(
                        name = dialog.data.mainButton.name,
                        id = dialog.data.mainButton.action
                    )
                    secondaryBtn = secondaryBtn(
                        name = dialog.data.alternativeButton?.name,
                        id = dialog.data.alternativeButton?.action
                    )
                }

                ALERT_TYPE_LEFT_MIDDLE -> {
                    primaryBtn = primaryBtn(
                        name = dialog.data.mainButton.name,
                        id = dialog.data.mainButton.action
                    )
                }

                else -> {
                    val hasAlternativeBtn = dialog.data.alternativeButton != null
                    primaryBtn = if (hasAlternativeBtn) {
                        val id = dialog.data.mainButton.action
                        val title = dialog.data.mainButton.name ?: ""
                        BtnPrimaryDefaultAtmData(
                            title = UiText.DynamicString(title),
                            id = id.orEmpty()
                        )
                    } else null
                    secondaryBtn = if (dialog.data.alternativeButton?.name != null) {
                        dialog.data.alternativeButton?.action?.let { id ->
                            dialog.data.alternativeButton?.name?.let { name ->
                                val title = UiText.DynamicString(name)
                                BtnPlainAtmData(title = title, id = id)
                            }
                        }
                    } else null
                    strokeBtn = if (!hasAlternativeBtn) {
                        val id = dialog.data.mainButton.action
                        val title = UiText.DynamicString(dialog.data.mainButton.name ?: "")
                        BtnStrokeLargeAtmData(title = title, id = id.orEmpty())
                    } else null
                }
            }

            _templateMlcV1Data.value = TemplateDialogScreenData(
                alignment = alignment,
                icon = dialog.data.icon?.let { UiText.DynamicString(it) },
                titleText = dialog.data.title.let { UiText.DynamicString(it) },
                descriptionText = dialog.data.description?.let {
                    UiText.DynamicString(it)
                },
                primaryButton = primaryBtn,
                secondaryButton = secondaryBtn,
                strokeButton = strokeBtn,
                isCloseable = dialog.isClosable
            )
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.BUTTON_REGULAR -> {
                uiAction.data?.let { performDialogAction(it) }
            }

            UIActionKeysCompose.BTN_PLAIN_ATM -> {
                uiAction.data?.let { performDialogAction(it) }
            }

            UIActionKeysCompose.CLOSE_BUTTON -> {
                doCloseDialog()
            }
        }
    }

    fun doLogout() {
        actionLogout.postValue(UiEvent())
    }

    private fun primaryBtn(name: String?, id: String?): BtnPrimaryDefaultAtmData? {
        return id?.let { pbId ->
            name?.let { mbName ->
                val title = UiText.DynamicString(mbName)
                BtnPrimaryDefaultAtmData(title = title, id = pbId)
            }
        }
    }

    private fun secondaryBtn(name: String?, id: String?): BtnPlainAtmData? {
        return id?.let { sbId ->
            name?.let { name ->
                val title = UiText.DynamicString(name)
                BtnPlainAtmData(title = title, id = sbId)
            }
        }
    }

    private fun strokeBtn(name: String?, id: String?): BtnStrokeLargeAtmData? {
        return id?.let { sbId ->
            name?.let { name ->
                val title = UiText.DynamicString(name)
                BtnStrokeLargeAtmData(title = title, id = sbId)
            }
        }
    }

    private fun performDialogAction(dialogAction: String) {
        viewModelScope.launch {
            when (dialogAction) {
                DIALOG_ACTION_CODE_LOGOUT -> {
                    actionLogout.postValue(UiEvent())
                }

                else -> {
                    _navigation.tryEmit(Navigation.OnDialogAction(dialogAction))
                }
            }
        }
    }

    private fun doCloseDialog() {
        _navigation.tryEmit(Navigation.DismissDialog)
    }

    sealed class Navigation : NavigationPath {
        data object DismissDialog : Navigation()
        data class OnDialogAction(val action: String) : Navigation()
    }

}