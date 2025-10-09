package ua.gov.diia.core.models.acquirer

enum class AcquirerServiceType(val id: String) {
    DOCUMENT_GENERATION_BARCODE("documentsGeneration"),
    IDENTITY_CHECK("identityCheck"),
    UNKNOWN(""),
}