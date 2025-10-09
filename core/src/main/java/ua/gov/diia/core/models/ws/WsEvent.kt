package ua.gov.diia.core.models.ws

import com.squareup.moshi.Json

abstract class WsEvent(
    @property:Json(name = "event")
    var event: String
)