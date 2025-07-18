import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.diary")
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.accompanist.permissions)
}