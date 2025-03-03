import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.lang.String

plugins {
    id("ssukssukdiary.android.application")
}

android {
    namespace = "com.sabo.ssukssukdiary"

    defaultConfig {
        targetSdk = 35
        applicationId = "com.sabo.ssukssukdiary"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "KAKAO_APP_KEY", String.valueOf(getKakaoSdkAppKey()))
            buildConfigField("String", "NAVER_CLIENT_ID", String.valueOf(getNaverClientId()))
            buildConfigField(
                "String",
                "NAVER_CLIENT_SECRET",
                String.valueOf(getNaverClientSecret())
            )
            manifestPlaceholders["KAKAO_APP_KEY"] = String.valueOf(getKakaoSdkAppKey())
        }
        getByName("release") {
            buildConfigField("String", "KAKAO_APP_KEY", String.valueOf(getKakaoSdkAppKey()))
            buildConfigField("String", "NAVER_CLIENT_ID", String.valueOf(getNaverClientId()))
            buildConfigField(
                "String",
                "NAVER_CLIENT_SECRET",
                String.valueOf(getNaverClientSecret())
            )
            manifestPlaceholders["KAKAO_APP_KEY"] = String.valueOf(getKakaoSdkAppKey())
        }
    }
}

dependencies {
    implementation(projects.feature.main)
    implementation(projects.feature.login)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)

    implementation(libs.bundles.login)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}

fun getKakaoSdkAppKey(): kotlin.String {
    return gradleLocalProperties(rootDir, providers).getProperty("KAKAO_APP_KEY") ?: ""
}

fun getNaverClientId(): kotlin.String {
    return gradleLocalProperties(rootDir, providers).getProperty("NAVER_CLIENT_ID") ?: ""
}

fun getNaverClientSecret(): kotlin.String {
    return gradleLocalProperties(rootDir, providers).getProperty("NAVER_CLIENT_SECRET") ?: ""
}