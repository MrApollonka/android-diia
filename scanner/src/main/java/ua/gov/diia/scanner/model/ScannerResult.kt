package ua.gov.diia.scanner.model

sealed interface ScannerResult {
    data class BarcodeDetected(val barcode: String) : ScannerResult
    data object BarcodeNotDetected : ScannerResult
}