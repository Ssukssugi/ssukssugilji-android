import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
    id("kotlinx-serialization")
}

android {
    setNameSpace("core.android")
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}