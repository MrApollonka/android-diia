package ua.gov.diia.core.util.sms

import kotlinx.coroutines.flow.Flow

interface SmsClient {

    val smsCode: Flow<String>

    fun startSmsClient()

    fun destroy()

}