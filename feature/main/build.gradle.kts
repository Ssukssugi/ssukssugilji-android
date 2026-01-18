import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.main")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.toolkit)
    implementation(projects.feature.login)
    implementation(projects.feature.signup)
    implementation(projects.feature.home)
    implementation(projects.feature.diary)
    implementation(projects.feature.profile)
    implementation(projects.feature.town)
}
