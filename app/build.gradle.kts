import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.lang.String

plugins {
    id("ssukssukdiary.android.application")
    alias(libs.plugins.google.service)
}

android {
    namespace = "com.sabo.ssukssukdiary"

    defaultConfig {
        applicationId = "com.sabo.ssukssukdiary"
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        create("release") {
            storeFile = file("ssukssukdiary-keystore.jks")
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("SIGNING_STORE_PASSWORD")
            keyAlias =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_ALIAS")
            keyPassword =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_PASSWORD")
        }
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
            isMinifyEnabled = false
        }
        getByName("release") {
            buildConfigField("String", "KAKAO_APP_KEY", String.valueOf(getKakaoSdkAppKey()))
            buildConfigField("String", "NAVER_CLIENT_ID", String.valueOf(getNaverClientId()))
            buildConfigField(
                "String",
                "NAVER_CLIENT_SECRET",
                String.valueOf(getNaverClientSecret())
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["KAKAO_APP_KEY"] = String.valueOf(getKakaoSdkAppKey())
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.feature.main)
    implementation(projects.feature.login)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)

    implementation(platform(libs.firebase.bom))
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
