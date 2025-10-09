package ua.gov.diia.verification.util

import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.throwExceptionInDebugOrShowAlert
import javax.inject.Inject

class AndroidClientAlertDialogsFactory @Inject constructor(
) : ClientAlertDialogsFactory {

    override fun showCustomAlert(keyAlert: String, isClosable: Boolean): TemplateDialogModel {
        return when (keyAlert) {

            NO_VERIFICATION_METHODS -> TemplateDialogModel(
                ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                "middleCenterAlignAlert",
                isClosable = true,
                TemplateDialogData(
                    "\uD83D\uDE1E",
                    "Неможливо підтвердити особу користувача",
                    "На жаль, на поточний момент немає доступних методів підтвердження особи користувача для створення Дія.Підпису. Спробуйте пізніше.",
                    TemplateDialogButton("Зрозуміло", null, ActionsConst.DIALOG_ACTION_CODE_SKIP)
                )
            )

            else -> "Unhandled keyAlert".throwExceptionInDebugOrShowAlert(isClosable)
        }
    }

    companion object {
        const val UNKNOWN_ERR = "unknownErrorAlert"
        const val NO_VERIFICATION_METHODS = "getNoVerificationMethodsDialog"
    }
}