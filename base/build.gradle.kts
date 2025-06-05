plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}
val libVersion = "3.0.0"
val devApi by configurations.creating
android {
    namespace = "com.koai.base"
    compileSdk = 35

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    publishing {
        singleVariant("prodRelease") {
            withSourcesJar()
            withJavadocJar()
        }
        singleVariant("devRelease") {
            withSourcesJar()
            withJavadocJar()
        }
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

    api("androidx.core:core-ktx:1.16.0")
    api("androidx.appcompat:appcompat:1.7.1")
    api("com.google.android.material:material:1.12.0")
    api("androidx.constraintlayout:constraintlayout:2.2.1")
    api("androidx.recyclerview:recyclerview:1.4.0")
    api("androidx.navigation:navigation-fragment-ktx:2.9.0")
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // firebase
    api("com.google.firebase:firebase-analytics:22.4.0")
    api("com.google.firebase:firebase-crashlytics:19.4.3")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.2.1")
    androidTestApi("androidx.test.espresso:espresso-core:3.6.1")

    // retrofit
    api("com.squareup.retrofit2:retrofit:3.0.0")
    api("com.google.code.gson:gson:2.13.1")
    api("com.squareup.retrofit2:converter-gson:3.0.0")
    devApi("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.16")
    devApi("com.facebook.stetho:stetho:1.6.0")
    devApi("com.facebook.stetho:stetho-okhttp3:1.6.0")

    // coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // lifecycle
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    api("androidx.fragment:fragment-ktx:1.8.8")

    api("androidx.multidex:multidex:2.0.1")
    api("com.airbnb.android:lottie:6.6.6")

    // load image
    api("io.coil-kt:coil:2.7.0")

    // di
    api(platform("io.insert-koin:koin-bom:4.0.4"))
    api("io.insert-koin:koin-core")
    api("io.insert-koin:koin-android")
    api("io.insert-koin:koin-androidx-navigation")
    api("io.insert-koin:koin-android-compat")

    // paging
    api("androidx.paging:paging-runtime-ktx:3.3.6")
    api("androidx.paging:paging-common-ktx:3.3.6")

    // wm
    api("androidx.work:work-runtime-ktx:2.10.1")

    // new encrypt sharepreferences
    api("androidx.datastore:datastore-preferences:1.1.7")

    // Room components
    api("androidx.room:room-runtime:2.7.1")
    annotationProcessor("androidx.room:room-runtime:2.7.1")
    androidTestApi("androidx.room:room-testing:2.7.1")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("prodRelease") {
                groupId = "com.koai"
                artifactId = "base"
                version = libVersion

                afterEvaluate {
                    from(components["prodRelease"])
                }
            }

            register<MavenPublication>("devRelease") {
                groupId = "com.koai"
                artifactId = "base"
                version = libVersion

                afterEvaluate {
                    from(components["devRelease"])
                }
            }
        }
    }
}

tasks.register("localBuild") {
    dependsOn("assembleProRelease")
}

tasks.register("createReleaseTag") {
    doLast {
        val tagName = "v$libVersion"
        try {
            providers.exec {
                commandLine("git", "tag", "-a", tagName, "-m", "Release tag $tagName")
            }

            providers.exec {
                commandLine("git", "push", "origin", tagName)
            }
        } catch (e: Exception) {
            println(e.toString())
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
    dependsOn("publishReleasePublicationToMavenRepository")
    val assembleReleaseTask = getTasksByName("localBuild", false).stream().findFirst().orElse(null)
    if (assembleReleaseTask != null) {
        assembleReleaseTask.mustRunAfter("clean")
        assembleReleaseTask.finalizedBy("publishReleasePublicationToMavenRepository")
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
