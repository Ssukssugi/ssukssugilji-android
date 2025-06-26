import com.sabo.convention.setNameSpace

plugins {
    id("ssukssukdiary.android.feature")
}

android {
    setNameSpace("feature.home")
}

dependencies {
    debugImplementation(libs.ui.tooling)
}