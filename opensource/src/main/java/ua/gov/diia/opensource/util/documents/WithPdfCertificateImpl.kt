package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class WithPdfCertificateImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs
) : WithPdfCertificate {

    override val shareCertificatePdfEventFlow = mutableSharedFlowOf<GeneratePdfFromDoc>()

    override suspend fun loadCertificatePdf(
        cert: DiiaDocument
    ) {
        /* no-op */
    }

}