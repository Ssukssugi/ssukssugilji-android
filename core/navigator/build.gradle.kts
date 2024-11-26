import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
    id("kotlinx-serialization")
}

android {
    setNameSpace("core.navigator")
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}