package ua.gov.diia.scanner.di

import com.google.mlkit.vision.barcode.common.Barcode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.scanner.DefaultBarcodeScannerImpl
import ua.gov.diia.scanner.DiiaBarcodeScanner

@Module
@InstallIn(ViewModelComponent::class)
object ScannerModule {

    @Provides
    @BarcodeScannerEAN13
    fun provideEAN13BarcodeScannerImpl(): DiiaBarcodeScanner = DefaultBarcodeScannerImpl(
        barcodeFormats = listOf(Barcode.FORMAT_EAN_13)
    )

    @Provides
    @BarcodeScannerCODE128
    fun provideCODE128BarcodeScannerImpl(): DiiaBarcodeScanner = DefaultBarcodeScannerImpl(
        barcodeFormats = listOf(Barcode.FORMAT_CODE_128)
    )

}