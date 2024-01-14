plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.koai.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true
        aarMetadata {
            minCompileSdk = 29
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    api("androidx.core:core-ktx:1.12.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.11.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")

    //firebase
    api("com.google.firebase:firebase-analytics:21.5.0")
    api("com.google.firebase:firebase-auth:22.3.0")
    api("com.google.firebase:firebase-crashlytics:18.6.0")
    api("androidx.navigation:navigation-fragment-ktx:2.7.6")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")

    //retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.google.code.gson:gson:2.10.1")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    api("com.facebook.stetho:stetho:1.6.0")
    api("com.facebook.stetho:stetho-okhttp3:1.6.0")

    //coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //lifecycle
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    api ("androidx.fragment:fragment-ktx:1.6.2")

    api("androidx.multidex:multidex:2.0.1")
}