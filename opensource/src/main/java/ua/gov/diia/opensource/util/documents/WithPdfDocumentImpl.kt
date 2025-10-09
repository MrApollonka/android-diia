package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class WithPdfDocumentImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs
) : WithPdfDocument {

    override val shareDocumentPdfEventFlow = mutableSharedFlowOf<GeneratePdfFromDoc>()

    override suspend fun loadDocumentPdf(
        document: DiiaDocument,
        loadDocumentErrorCallback: (template: TemplateDialogModel) -> Unit
    ) {
        val documentPdf = apiDocs.getDocumentPdf(
            document.getItemType(),
            document.docId()
        )

        if (documentPdf.documentFile != null) {
            shareDocumentPdfEventFlow.emit(
                GeneratePdfFromDoc(
                    docPDF = documentPdf.documentFile?.file.orEmpty(),
                    name = documentPdf.documentFile?.name.orEmpty()
                )
            )
        } else {
            documentPdf.template?.let { loadDocumentErrorCallback(it) }
        }
    }

}