import com.sabo.convention.configureHiltAndroid
import com.sabo.convention.configureKotlinAndroid

plugins {
    id("com.android.application")
}

android {
    lint {
        disable += "NullSafeMutableLiveData"
    }
}

configureKotlinAndroid()
configureHiltAndroid()