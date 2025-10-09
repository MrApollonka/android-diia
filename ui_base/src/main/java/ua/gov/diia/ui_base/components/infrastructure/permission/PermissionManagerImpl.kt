package ua.gov.diia.ui_base.components.infrastructure.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class PermissionsManagerImpl(
    private val activity: Activity,
) : PermissionsManager {
    @Composable
    override fun getLauncher(onResult: (Boolean) -> Unit): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = onResult,
        )
    }

    @Composable
    override fun getPermissionsLauncher(
        onResult: (Map<String, Boolean>) -> Unit,
    ): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = onResult,
        )

    override fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            activity,
            permission,
        ) == PackageManager.PERMISSION_GRANTED

    override fun checkPermissions(permissions: Array<String>): Boolean =
        permissions.map { checkPermission(it) }.all { isGranted -> isGranted }

    override fun shouldShowRequestPermissionRationale(
        permission: String,
    ): Boolean =
        activity.shouldShowRequestPermissionRationale(permission)

    override fun goToAppSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        startActivity(activity, intent, null)
    }
}
