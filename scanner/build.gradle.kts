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

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.constraint.layout.compose)
}