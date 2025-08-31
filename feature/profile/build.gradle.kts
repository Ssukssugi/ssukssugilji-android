import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.profile")
}

dependencies {
    implementation(projects.core.data)
}