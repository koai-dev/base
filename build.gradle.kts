buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.android.tools.build:gradle:8.3.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.3.1" apply false
}