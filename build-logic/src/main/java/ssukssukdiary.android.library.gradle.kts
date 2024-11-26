import com.sabo.convention.configureCoroutineAndroid
import com.sabo.convention.configureHiltAndroid
import com.sabo.convention.configureKotlinAndroid

plugins {
    id("com.android.library")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()