plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.faq"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(projects.libs.core)
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)
    implementation(projects.libs.diiaStorage)
}