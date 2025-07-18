import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.model")
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}