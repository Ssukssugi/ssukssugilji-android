import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.main")
}

dependencies {
    implementation(projects.feature.login)
    implementation(projects.feature.signup)
    implementation(projects.feature.home)
}
