import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.home")
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.toolkit)
}