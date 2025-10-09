package ua.gov.diia.opensource.di.delegate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.core.util.delegation.WithPushHandling
import ua.gov.diia.core.util.delegation.WithPushNotification
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.opensource.payment.delegation.DownloadAndShareReceipt
import ua.gov.diia.opensource.payment.delegation.DownloadAndShareReceiptOnFlow
import ua.gov.diia.opensource.util.delegation.DefaultDeeplinkHandleBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultErrorHandlingBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultErrorHandlingBehaviourOnFlow
import ua.gov.diia.opensource.util.delegation.DefaultPushHandlerBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultPushNotificationBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultRatingDialogBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultRatingDialogBehaviourOnFlow
import ua.gov.diia.opensource.util.delegation.DefaultRetryLastActionBehaviour
import ua.gov.diia.opensource.util.delegation.DefaultSelfPermissionBehavior
import ua.gov.diia.opensource.util.delegation.DefaultWithContextMenuBehaviour
import ua.gov.diia.payment.delegation.WithShareReceipt
import ua.gov.diia.payment.delegation.WithShareReceiptOnFlow

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelDelegationModule {

    @Binds
    fun bindContextMenuDelegate(
        impl: DefaultWithContextMenuBehaviour<ContextMenuField>
    ): WithContextMenu<ContextMenuField>

    @Binds
    fun bindErrorHandlerDelegate(
        impl: DefaultErrorHandlingBehaviour
    ): WithErrorHandling

    @Binds
    fun bindRetryDelegate(
        impl: DefaultRetryLastActionBehaviour
    ): WithRetryLastAction

    @Binds
    fun bindPushNotificationDelegate(
        impl: DefaultPushNotificationBehaviour
    ): WithPushNotification

    @Binds
    fun bindPushHandler(
        impl: DefaultPushHandlerBehaviour
    ): WithPushHandling

    @Binds
    fun bindDeeplinkHandler(
        impl: DefaultDeeplinkHandleBehaviour
    ): WithDeeplinkHandling

    @Binds
    fun bindRatingDialogDelegate(
        impl: DefaultRatingDialogBehaviour
    ): WithRatingDialog

    @Binds
    fun bindRatingDialogDelegateOnFlow(
        impl: DefaultRatingDialogBehaviourOnFlow
    ): WithRatingDialogOnFlow

    @Binds
    fun bindErrorHandlerDelegateOnFlow(
        impl: DefaultErrorHandlingBehaviourOnFlow
    ): WithErrorHandlingOnFlow

    @Binds
    fun bindPermissionDelegate(
        impl: DefaultSelfPermissionBehavior
    ): WithPermission

    @Binds
    fun bindReceiptShare(
        impl: DownloadAndShareReceipt
    ): WithShareReceipt

    @Binds
    fun bindReceiptShareOnFlow(
        impl: DownloadAndShareReceiptOnFlow
    ): WithShareReceiptOnFlow

}