import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.signup")
}

dependencies {
    implementation(projects.core.data)
}
