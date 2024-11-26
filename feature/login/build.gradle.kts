import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.login")
}

dependencies {
    implementation(libs.bundles.login)
}
