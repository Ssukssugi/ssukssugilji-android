import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.data")
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(libs.retrofit)
}