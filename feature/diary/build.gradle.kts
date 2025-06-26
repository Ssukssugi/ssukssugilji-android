import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.diary")
}

dependencies {
    implementation(libs.accompanist.permissions)
}