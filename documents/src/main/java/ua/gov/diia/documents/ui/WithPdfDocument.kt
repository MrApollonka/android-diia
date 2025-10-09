package ua.gov.diia.documents.ui

import kotlinx.coroutines.flow.SharedFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc

interface WithPdfDocument {

    val shareDocumentPdfEventFlow: SharedFlow<GeneratePdfFromDoc>

    /**
     * loads pdf document and provides it into documentPdf LiveData
     * Leave as empty if you do not need to load pdf
     */
    suspend fun loadDocumentPdf(
        document: DiiaDocument,
        loadDocumentErrorCallback: (template: TemplateDialogModel) -> Unit
    )

}