import com.sabo.convention.configureHiltAndroid
import com.sabo.convention.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()