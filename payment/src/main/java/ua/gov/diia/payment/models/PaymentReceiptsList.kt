package ua.gov.diia.payment.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.DownloadableDocument
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import java.util.UUID

@JsonClass(generateAdapter = true)
data class PaymentReceiptsList(
    @Json(name = "receipts")
    val receipts: List<PaymentReceiptUrl>?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)

@JsonClass(generateAdapter = true)
data class PaymentReceiptUrl(
    @Json(name = "name")
    val name: String?,
    @Json(name = "link")
    val receiptUrl: String
) : DownloadableDocument {

    override val id: String
        get() = UUID.randomUUID().toString()

    override val docName: String
        get() = name ?: ""

    override val downloadUrl: String
        get() = receiptUrl ?: ""

}