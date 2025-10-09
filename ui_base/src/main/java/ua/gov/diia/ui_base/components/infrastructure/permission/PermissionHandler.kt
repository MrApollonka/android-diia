package ua.gov.diia.ui_base.components.infrastructure.permission

import android.app.Activity
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.common.template_dialogs.SystemDialogData
import ua.gov.diia.core.util.delegation.Permission
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtomData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.SystemTextColor
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.provider.LocalPermissionsManager

@Composable
fun PermissionHandler(
    permission: ConsumableItem?,
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    ),
    onPermissionGranted: (Permission) -> Unit,
    onPermissionDenied: (Permission) -> Unit,
) {
    val context = LocalContext.current
    val activity: Activity =
        (context as ContextWrapper).baseContext as Activity
    CompositionLocalProvider(
        LocalPermissionsManager provides PermissionsManagerImpl(activity)
    ) {
        val permissionsManager = LocalPermissionsManager.current
        val rationaleUserDialog =
            remember { mutableStateOf<Pair<SystemDialogData, Boolean>?>(null) }
        val permissionUserDialog =
            remember { mutableStateOf<PermissionAlertDialogData?>(null) }
        val permissionLauncher = permissionsManager.getPermissionsLauncher(
            onResult = { resultMap ->
                resultMap.forEach { (entity, isGranted) ->
                    if (isGranted) {
                        onPermissionGranted.invoke(permission!!.item as Permission)
                    } else {
                        onPermissionDenied.invoke(permission!!.item as Permission)
                    }
                }
            })

        if (rationaleUserDialog.value?.second == true) {
            rationaleUserDialog.value?.first?.let { dialogData ->
                UserDialog(
                    dialogText = dialogData.message?.let { context.getString(it) },
                    dialogTitle = dialogData.title?.let { context.getString(it) },
                    confirmBtnTitle = dialogData.positiveButtonTitle?.let { context.getString(it) },
                    cancelBtnTitle = dialogData.negativeButtonTitle?.let { context.getString(it) },
                    onConfirmation = {
                        rationaleUserDialog.value =
                            rationaleUserDialog.value?.copy(second = false)
                        permissionsManager.goToAppSettings()
                    },
                    onDismissRequest = {
                        rationaleUserDialog.value =
                            rationaleUserDialog.value?.copy(second = false)
                    }
                )
            }
        }

        if (permissionUserDialog.value?.showDialog == true) {
            permissionUserDialog.value?.dialogData?.let { dialogData ->
                UserDialog(
                    dialogText = dialogData.message?.let { context.getString(it) },
                    dialogTitle = dialogData.title?.let { context.getString(it) },
                    confirmBtnTitle = dialogData.positiveButtonTitle?.let { context.getString(it) },
                    cancelBtnTitle = dialogData.negativeButtonTitle?.let { context.getString(it) },
                    onConfirmation = {
                        permissionUserDialog.value?.let {
                            permissionLauncher.launch(it.permission.value)
                        }
                        permissionUserDialog.value =
                            permissionUserDialog.value?.copy(showDialog = false)
                    },
                    onDismissRequest = {
                        permissionUserDialog.value =
                            permissionUserDialog.value?.copy(showDialog = false)
                    },
                    dialogProperties = dialogProperties
                )
            }
        }

        LaunchedEffect(permission) {
            if (permission != null && permission.isNotConsumed()) {
                permission.consumeEvent<Permission> { permission ->
                    val granted = permission.value.all { permissionsManager.checkPermission(it) }
                    if (granted) {
                        permission.value.forEach { _ ->
                            onPermissionGranted.invoke(permission)
                        }
                    } else {
                        if (permission.value.size > 1) {
                            val shouldShowRationale = permission.value.map {
                                permissionsManager.shouldShowRequestPermissionRationale(it)
                            }
                            val partiallyGranted =
                                permission.value.any { permissionsManager.checkPermission(it) }
                            if (shouldShowRationale.all { it }) {
                                rationaleUserDialog.value = Pair(permission.rationaleDialog, true)
                            } else if (partiallyGranted) {
                                permissionLauncher.launch(permission.value)
                            } else {
                                permissionUserDialog.value =
                                    PermissionAlertDialogData(
                                        permission.permissionDialog,
                                        permission,
                                        true
                                    )
                            }
                        } else {
                            if (permissionsManager.shouldShowRequestPermissionRationale(permission.value.first())) {
                                rationaleUserDialog.value = Pair(permission.rationaleDialog, true)
                            } else {
                                permissionUserDialog.value =
                                    PermissionAlertDialogData(
                                        permission.permissionDialog,
                                        permission,
                                        true
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun UserDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String?,
    dialogText: String?,
    confirmBtnTitle: String?,
    cancelBtnTitle: String?,
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    )
) {
    Dialog(
        properties = dialogProperties,
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (dialogTitle != null) {
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 24.dp),
                        text = dialogTitle,
                        color = Black,
                        fontSize = 20.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (dialogText != null) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        text = dialogText,
                        color = SystemTextColor,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (cancelBtnTitle != null) {
                        ButtonSystemAtom(
                            data = ButtonSystemAtomData(
                                title = cancelBtnTitle.toDynamicString(),

                                ), onUIAction = {
                                onDismissRequest.invoke()
                            }
                        )
                    }
                    if (confirmBtnTitle != null) {
                        ButtonSystemAtom(
                            data = ButtonSystemAtomData(
                                title = confirmBtnTitle.toDynamicString(),

                                ), onUIAction = {
                                onConfirmation.invoke()
                            }
                        )
                    }
                }
            }
        }
    }
}

data class PermissionAlertDialogData(
    val dialogData: SystemDialogData,
    val permission: Permission,
    val showDialog: Boolean
)


/**
 * Preview will fail because PermissionHandler will try to use activity context (not available
 * from preview). But you still can understand how to use permissionFlow from PreviewViewModel.
 */
@Preview
@Composable
fun PermissionHandlerPreview() {
    val context = LocalContext.current
    val previewVM = PreviewViewModel()
    val permissionFlow = previewVM.permissionFlow.collectAsState(null)
    Column {
        PermissionHandler(
            permission = permissionFlow.value,
            onPermissionGranted = { permission ->
                Toast.makeText(context, "Permission ${permission.name} granted", Toast.LENGTH_SHORT).show()
            },
            onPermissionDenied = { permission ->
                Toast.makeText(context, "Permission ${permission.name} denied", Toast.LENGTH_SHORT).show()
            }
        )
        Button(onClick = {
            previewVM.onRequestPermissionTriggered()
        }) {
            Text("Request Camera permission")
        }
    }
}

private class PreviewViewModel() {
    private val _permissionFlow = MutableStateFlow<ConsumableItem?>(null)
    val permissionFlow = _permissionFlow.asSharedFlow()

    fun onRequestPermissionTriggered() {
        _permissionFlow.tryEmit(ConsumableItem(Permission.CAMERA))
    }
}
