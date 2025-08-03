buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.3")
        classpath("com.android.tools.build:gradle:8.11.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.5")
        classpath("com.google.firebase:perf-plugin:2.0.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0" apply false
}