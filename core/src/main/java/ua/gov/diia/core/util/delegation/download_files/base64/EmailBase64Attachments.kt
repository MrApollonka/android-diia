package ua.gov.diia.core.util.delegation.download_files.base64

class EmailBase64Attachments(
    val attachments: List<DownloadableBase64File>,
    val recipients: List<String>? = null,
    val subject: String? = null,
    val body: String? = null
)