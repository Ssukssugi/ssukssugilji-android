import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.town")
}

dependencies {
    implementation(projects.core.data)
}