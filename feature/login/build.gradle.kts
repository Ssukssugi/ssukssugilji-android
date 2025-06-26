import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.sabo.convention.setNameSpace
import java.lang.String

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.login")

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "GOOGLE_CLIENT_ID", String.valueOf(getGoogleClientId()))
        }
        getByName("release") {
            buildConfigField("String", "GOOGLE_CLIENT_ID", String.valueOf(getGoogleClientId()))
        }
    }
}

dependencies {
    implementation(libs.bundles.login)
}

fun getGoogleClientId(): kotlin.String {
    return gradleLocalProperties(rootDir, providers).getProperty("GOOGLE_CLIENT_ID") ?: ""
}
