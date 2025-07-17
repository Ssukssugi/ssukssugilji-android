@file:Suppress("DSL_SCOPE_VIOLATION", "-opt-in=kotlinx.serialization.InternalSerializationApi")

gradle.startParameter.excludedTaskNames.addAll(
    gradle.startParameter.taskNames.filter { it.contains("testClasses") }
)

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.google.service) apply false
}