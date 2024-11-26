import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.main")
}

dependencies {
    implementation(project(":feature:login"))
}
