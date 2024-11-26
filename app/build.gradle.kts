import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.lang.String

plugins {
    id("ssukssukdiary.android.application")
}

android {
    namespace = "com.sabo.ssukssukdiary"

    defaultConfig {
        applicationId = "com.sabo.ssukssukdiary"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        buildTypes {
            getByName("debug") {
                buildConfigField("String", "KAKAO_APP_KEY", String.valueOf(getKakaoSdkAppKey()))
            }
            getByName("release") {
                buildConfigField("String", "KAKAO_APP_KEY", String.valueOf(getKakaoSdkAppKey()))
            }
        }
    }
}

dependencies {
    implementation(project(":feature:main"))
    implementation(project(":feature:login"))
    implementation(project(":core:designsystem"))

    implementation(libs.bundles.login)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}

fun getKakaoSdkAppKey(): kotlin.String {
    return gradleLocalProperties(rootDir, providers).getProperty("KAKAO_APP_KEY") ?: ""
}