plugins {
    alias(libs.plugins.android.library)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "org.gaziz.birgram.core.navigation"
    compileSdk {
        version = release(37)
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
    //Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    //Junit and tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}