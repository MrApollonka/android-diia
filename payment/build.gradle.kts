plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
}

android {
    namespace = "ua.gov.diia.payment"

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)
}