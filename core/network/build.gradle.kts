import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.network")

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", getApiBaseUrl())
        }
        getByName("release") {
            buildConfigField("String", "BASE_URL", getApiBaseUrl())
        }
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.bundles.network)
}

fun getApiBaseUrl(): String {
    return gradleLocalProperties(rootDir, providers).getProperty("BASE_URL") ?: ""
}