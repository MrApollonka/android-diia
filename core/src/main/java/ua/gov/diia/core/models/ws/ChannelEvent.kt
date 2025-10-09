package ua.gov.diia.core.models.ws

data class ChannelEvent(
    val data: String?,
    val t: Throwable? = null
)