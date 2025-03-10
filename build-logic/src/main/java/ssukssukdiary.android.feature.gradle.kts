import com.sabo.convention.implementation

plugins {
    id("ssukssukdiary.android.library")
    id("ssukssukdiary.android.compose")
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigator"))
    implementation(project(":core:android"))
    implementation(project(":core:domain"))
}