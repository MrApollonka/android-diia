plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.web"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.browser)
}