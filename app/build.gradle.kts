plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.shubham.movie_mania_upgrade"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shubham.movie_mania_upgrade"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
     // libraries additions permission library
    implementation(project(":lib_permission"))
    implementation(project(":lib_speechRecognizer"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // navigation dependencies
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    // Di dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    // retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // pagination dependencies
    implementation(libs.androidx.pagination3)
    // glide dependencies
    implementation(libs.bumptech.glide)
    // dynamic versions
    implementation(libs.ssp)
    implementation(libs.sdp)
    // logging interceptor
    implementation(libs.logging.interceptor)
    // splash screen
    implementation(libs.splash.screen)
    //logger
    implementation(libs.timber)

}