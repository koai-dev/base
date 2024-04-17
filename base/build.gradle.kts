import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
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
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    api("androidx.core:core-ktx:1.12.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.11.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.recyclerview:recyclerview:1.3.2")

    // firebase
    api("com.google.firebase:firebase-analytics:21.6.2")
    api("com.google.firebase:firebase-auth:22.3.1")
    api("com.google.firebase:firebase-crashlytics:18.6.4")
    api("androidx.navigation:navigation-fragment-ktx:2.7.7")
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")

    // retrofit
    api("com.squareup.retrofit2:retrofit:2.11.0")
    api("com.google.code.gson:gson:2.10.1")
    api("com.squareup.retrofit2:converter-gson:2.11.0")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.12")
    api("com.facebook.stetho:stetho:1.6.0")
    api("com.facebook.stetho:stetho-okhttp3:1.6.0")

    // coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // lifecycle
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    api("androidx.fragment:fragment-ktx:1.6.2")

    api("androidx.multidex:multidex:2.0.1")
    api("com.airbnb.android:lottie:6.4.0")

    // load image
    api("io.coil-kt:coil:2.6.0")

    // di
    api(platform("io.insert-koin:koin-bom:3.5.4"))
    api("io.insert-koin:koin-core")
    api("io.insert-koin:koin-android")
    api("io.insert-koin:koin-androidx-navigation")
    api("io.insert-koin:koin-android-compat")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.koai"
                artifactId = "base"
                version = "1.4.2"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
        repositories {
            val propsFile = rootProject.file("github.properties")
            val props = Properties()
            props.load(FileInputStream(propsFile))
            maven(url = "${props["mavenRepo"]}") {
                credentials {
                    username = props["username"].toString()
                    password = props["token"].toString()
                }
            }
        }
    }
}

tasks.register("localBuild") {
    dependsOn("assembleRelease")
}

tasks.register("createReleaseTag") {
    doLast {
        val tagName = "v$version"
        try {
            exec {
                commandLine("git", "tag", "-a", tagName, "-m", "Release tag $tagName")
            }

            exec {
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
