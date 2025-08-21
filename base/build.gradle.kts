plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}
val libVersion = "4.0.1"
android {
    namespace = "com.koai.base"
    compileSdk = 36

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
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

dependencies {

    api("androidx.core:core-ktx:1.16.0")
    api("androidx.appcompat:appcompat:1.7.1")
    api("com.google.android.material:material:1.12.0")
    api("androidx.constraintlayout:constraintlayout:2.2.1")
    api("androidx.recyclerview:recyclerview:1.4.0")
    api("androidx.navigation:navigation-fragment-ktx:2.9.3")
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // firebase
    api("com.google.firebase:firebase-analytics:23.0.0")
    api("com.google.firebase:firebase-crashlytics:20.0.0")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.3.0")
    androidTestApi("androidx.test.espresso:espresso-core:3.7.0")

    // retrofit
    api("com.squareup.retrofit2:retrofit:3.0.0")
    api("com.google.code.gson:gson:2.13.1")
    api("com.squareup.retrofit2:converter-gson:3.0.0")
    api("com.squareup.okhttp3:logging-interceptor:5.1.0")
    api("com.facebook.stetho:stetho:1.6.0")
    api("com.facebook.stetho:stetho-okhttp3:1.6.0")

    // coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // lifecycle
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
    api("androidx.fragment:fragment-ktx:1.8.8")

    api("androidx.multidex:multidex:2.0.1")
    api("com.airbnb.android:lottie:6.6.7")

    // load image
    api("io.coil-kt:coil:2.7.0")

    // di
    api(platform("io.insert-koin:koin-bom:4.1.0"))
    api("io.insert-koin:koin-core")
    api("io.insert-koin:koin-android")
    api("io.insert-koin:koin-androidx-navigation")
    api("io.insert-koin:koin-android-compat")

    // paging
    api("androidx.paging:paging-runtime-ktx:3.3.6")
    api("androidx.paging:paging-common-ktx:3.3.6")

    // wm
    api("androidx.work:work-runtime-ktx:2.10.3")

    // new encrypt sharepreferences
    api("androidx.datastore:datastore-preferences:1.1.7")

    // Room components
    api("androidx.room:room-runtime:2.7.2")
    annotationProcessor("androidx.room:room-runtime:2.7.2")
    androidTestApi("androidx.room:room-testing:2.7.2")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.koai"
                artifactId = "base"
                version = libVersion

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}

tasks.register("localBuildProd") {
    dependsOn("assembleProdRelease")
}

tasks.register("createReleaseTag") {
    doLast {
        val tagName = "v$libVersion"
        try {
            println("Creating tag: $tagName")

            providers
                .exec {
                    commandLine("git", "tag", "-a", tagName, "-m", "Release tag $tagName")
                }.result
                .get()

            providers
                .exec {
                    commandLine("git", "push", "origin", tagName)
                }.result
                .get()

            println("Successfully created and pushed tag: $tagName")
        } catch (e: Exception) {
            println("❌ Failed to create/push tag $tagName: ${e.message}")
        }
    }
}
/**
 * to build new version library: run in terminal
 *  ./gradlew cleanBuildPublish
 *
 */
tasks.register("cleanBuildPublish") {
    dependsOn("clean")
    dependsOn("localBuild")
    dependsOn("publishProdReleasePublicationToMavenRepository")
    val assembleReleaseTask =
        getTasksByName("localBuildProd", false).stream().findFirst().orElse(null)
    if (assembleReleaseTask != null) {
        assembleReleaseTask.mustRunAfter("clean")
        assembleReleaseTask.finalizedBy("publishProdReleasePublicationToMavenRepository")
    }
}

ktlint {
    version.set("1.6.0") // Or the latest
    android.set(true) // Enables Android-specific formatting rules
    outputColorName.set("GREEN") // Optional: terminal color
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    enableExperimentalRules.set(true) // Optional
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
