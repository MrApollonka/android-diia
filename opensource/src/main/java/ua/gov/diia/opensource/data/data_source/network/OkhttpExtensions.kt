package ua.gov.diia.opensource.data.data_source.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.setTimeout() {
    connectTimeout(TimeoutConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    writeTimeout(TimeoutConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
    readTimeout(TimeoutConstants.READ_TIMEOUT, TimeUnit.SECONDS)
}