package ua.gov.diia.core.util

import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst

fun throwExceptionInDebug(message: String) {
    if (isDebugMode()) {
        throw Exception(message)
    }
}

fun String.throwExceptionInDebugOrShowAlert(isClosable: Boolean): TemplateDialogModel {
    if (isDebugMode()) {
        throw Exception(this)
    }

    return TemplateDialogModel(
        type = "smallAlert",
        key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
        isClosable = isClosable,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "На жаль, сталася помилка",
            mainButton = TemplateDialogButton(
                name = if (isClosable) "Спробувати ще" else "Зрозуміло",
                action = if (isClosable) ActionsConst.GENERAL_RETRY else ActionsConst.ERROR_DIALOG_DEAL_WITH_IT,
            ),
        )
    )
}