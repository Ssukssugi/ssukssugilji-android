package com.sabo.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid() {

    pluginManager.apply("org.jetbrains.kotlin.android")
    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

    androidExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 33
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            getByName("release") {
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                freeCompilerArgs.add("-opt-in=kotlinx.serialization.InternalSerializationApi")
                freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
                freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            }
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}