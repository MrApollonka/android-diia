plugins {
    alias(libs.plugins.diia.android.library)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
    alias(libs.plugins.diia.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "ua.gov.diia.ui_base"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.libs.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.accompanist.pager)
    //retrofit
    implementation(libs.retrofit.core)
    //compose
    implementation(libs.androidx.constraint.layout.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.hls)

    gplayImplementation(libs.play.services.location)
    huaweiImplementation(libs.huawei.location)
    
    //Paging
    implementation(libs.androidx.paging.ktx)

    constraints {
        implementation(libs.kotlin.stdlib7) {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation(libs.kotlin.stdlib8) {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    // Moshi
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.better.link)

    implementation(libs.zxing)

    implementation(libs.reorderable)

    implementation(libs.cameraview)

    implementation(libs.coil.kt.compose)

    //test
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockk.android)
    testImplementation(libs.androidx.lifecycle.livedata.ktx)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}