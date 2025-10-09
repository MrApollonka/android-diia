package ua.gov.diia.scanner

import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.scanner.model.ScannerResult

class DefaultBarcodeScannerImpl(
    val barcodeFormats: List<Int>
) : DiiaBarcodeScanner {

    override val scanResultFlow = MutableSharedFlow<ScannerResult>()

    private var isRunning = false

    private lateinit var scanner: BarcodeScanner

    override suspend fun processImage(inputImage: InputImage) {
        if (!isRunning) {
            return
        }
        val text = inputToText(inputImage)
        if (text.isNotEmpty()) {
            scanResultFlow.emit(ScannerResult.BarcodeDetected(text))
        } else {
            scanResultFlow.emit(ScannerResult.BarcodeNotDetected)
        }
    }

    override fun init() {
        if (!isRunning) {
            val options = BarcodeScannerOptions.Builder()
                .setFormats(barcodeFormats)
                .build()
            scanner = BarcodeScanning.getClient(options)
            isRunning = true
        }
    }

    override fun dismiss() {
        if (isRunning) {
            isRunning = false
            scanner.close()
        }
    }

    private fun inputToText(inputImage: InputImage): String {
        var resultText = ""

        if (!this::scanner.isInitialized) {
            return resultText
        }

        try {
            val barcodes = Tasks.await(scanner.process(inputImage))

            for (barcode in barcodes) {
                val barcodeValue = barcode.displayValue ?: continue
                resultText = barcodeValue
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultText
    }

    private fun BarcodeScannerOptions.Builder.setFormats(formats: List<Int>): BarcodeScannerOptions.Builder {
        return when {
            formats.isEmpty() -> {
                setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            }

            formats.size == 1 -> {
                setBarcodeFormats(formats.first())
            }

            else -> {
                setBarcodeFormats(formats.first(), *formats.drop(1).toIntArray())
            }
        }
    }

}