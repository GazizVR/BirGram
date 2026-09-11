plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.3.7"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "org.gaziz.birgram.feature.chat"
    compileSdk {
        version = release(37)
    }
    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    //Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    //Internal modules
    implementation(project(":core:telegram"))
    //Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.video)
    //Lottie
    implementation(libs.lottie.compose)
    //ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui.compose.material3)
}