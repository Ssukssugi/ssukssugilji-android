import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.domain")
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}