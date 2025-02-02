import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.data")
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(libs.bundles.network)
}