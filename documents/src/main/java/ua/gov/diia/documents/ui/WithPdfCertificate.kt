package ua.gov.diia.documents.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc

interface WithPdfCertificate {

    val shareCertificatePdfEventFlow: MutableSharedFlow<GeneratePdfFromDoc>

    /**
     * loads pdf certificate and provides it into certificatePdf LiveData
     * Leave as empty if you do not need to load pdf
     */
    suspend fun loadCertificatePdf(cert: DiiaDocument)

}