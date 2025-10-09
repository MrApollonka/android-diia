plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.scanner"
}

dependencies {
    api(libs.mlkit.barcode)
    api(libs.cameraview)
}