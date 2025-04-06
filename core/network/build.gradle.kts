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
    implementation(libs.bundles.network)
    implementation(projects.core.domain)
}

fun getApiBaseUrl(): String {
    return gradleLocalProperties(rootDir, providers).getProperty("BASE_URL") ?: ""
}