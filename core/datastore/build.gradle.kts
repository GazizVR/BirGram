plugins {
    alias(libs.plugins.android.library)
    id("com.google.devtools.ksp") version "2.3.7"
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.3.21"
}

android {
    namespace = "org.gaziz.birgram.core.datastore"
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
    //Junit and tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    //DataStore
    implementation(libs.androidx.datastore.preferences)
    //Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
}