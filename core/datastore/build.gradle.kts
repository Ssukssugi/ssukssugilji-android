import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.library")
}

android {
    setNameSpace("core.datastore")
}

dependencies {
    implementation(libs.dataStore)
}