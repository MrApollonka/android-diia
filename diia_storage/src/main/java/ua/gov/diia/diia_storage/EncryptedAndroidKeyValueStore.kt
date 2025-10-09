package ua.gov.diia.diia_storage

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import ua.gov.diia.core.util.throwExceptionInDebug
import ua.gov.diia.diia_storage.model.KeyValueStore
import java.security.KeyStore

class EncryptedAndroidKeyValueStore(
    private val context: Context,
    private val configuration: PreferenceConfiguration,
) : AndroidKeyValueStore(), PinStore, ServiceUserUidStore {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = buildKeyValue()
    }

    private fun buildKeyValue(): SharedPreferences {
        return try {
            createSharedPreferences()
        } catch (e: Exception) {
            deleteMasterKey()
            deleteSharedPreferences(configuration.preferenceName)
            return createSharedPreferences()
        }
    }

    override fun getSharedPreferences(): SharedPreferences = sharedPreferences

    override fun allowedScopes() = configuration.allowedScopes

    override suspend fun savePin(pin: String) {
        sharedPreferences.edit().putString(PIN, pin).apply()
    }

    override suspend fun isPinValid(pinInput: String): Boolean {
        if (sharedPreferences.contains(PIN)) {
            val pin = sharedPreferences.getString(PIN, KeyValueStore.DEFAULT_STRING_VALUE)
            return pin == pinInput
        }
        return false
    }

    override fun pinPresent(): Boolean {
        return sharedPreferences.contains(PIN)
    }

    override suspend fun saveServiceUserUUID(uuid: String) {
        sharedPreferences.edit { putString(SERVICE_USER_UUID, uuid) }
    }

    override suspend fun getServiceUserUUID(): String? {
        val result = sharedPreferences.getString(SERVICE_USER_UUID, KeyValueStore.DEFAULT_STRING_VALUE)
        return if (result != KeyValueStore.DEFAULT_STRING_VALUE) {
            result
        } else {
            null
        }
    }

    override fun clear() {
        clearSharedPreference(configuration.preferenceName)
        sharedPreferences = buildKeyValue()
        set(CommonPreferenceKeys.IsFirst, false)
    }

    private fun createSharedPreferences(): SharedPreferences {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            configuration.preferenceName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun clearSharedPreference(prefName: String) {
        try {
            context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().clear().apply()
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                deleteMasterKey()
                context.deleteSharedPreferences(prefName)
            }
        }
    }

    private fun deleteSharedPreferences(prefName: String) {
        try {
            clearSharedPreference(prefName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.deleteSharedPreferences(prefName)
            }
        } catch (e: Exception) {
            throwExceptionInDebug(e.message ?: e.toString())
        }
    }

    private fun deleteMasterKey() {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            keyStore.deleteEntry("_androidx_security_master_key_")
        } catch (e: Exception) {
            throwExceptionInDebug(e.message ?: e.toString())
        }
    }

    private companion object {
        const val PIN = "pin"
        const val SERVICE_USER_UUID = "service_user_uuid"
    }

}
