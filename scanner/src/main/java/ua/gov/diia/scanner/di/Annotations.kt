package ua.gov.diia.scanner.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BarcodeScannerEAN13

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BarcodeScannerCODE128