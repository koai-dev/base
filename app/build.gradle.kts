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
    compileSdk = 35

    defaultConfig {
        applicationId = "com.koai.example"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    setFlavorDimensions(arrayListOf("default"))
    productFlavors {
        create("dev"){
            dimension = "default"
        }
        create("prod"){
            dimension = "default"
        }
    }
}

dependencies {
    implementation(project(":base"))

    //firebase
    implementation("com.google.firebase:firebase-perf:21.0.5")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.2")
    implementation("com.google.firebase:firebase-config:22.1.2")
}