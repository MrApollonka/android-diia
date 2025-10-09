package ua.gov.diia.scanner

import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.SharedFlow
import ua.gov.diia.scanner.model.ScannerResult

interface DiiaBarcodeScanner {

    val scanResultFlow: SharedFlow<ScannerResult>

    suspend fun processImage(inputImage: InputImage)

    fun init()

    fun dismiss()

}