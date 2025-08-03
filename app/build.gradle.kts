plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.koai.example"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.koai.example"
        minSdk = 24
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    setFlavorDimensions(arrayListOf("default"))
    productFlavors {
        create("dev") {
            dimension = "default"
        }
        create("prod") {
            dimension = "default"
        }
    }
}

dependencies {
    implementation(project(":base"))

    //firebase
    implementation("com.google.firebase:firebase-perf:22.0.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:22.0.0")
    implementation("com.google.firebase:firebase-config:23.0.0")
}