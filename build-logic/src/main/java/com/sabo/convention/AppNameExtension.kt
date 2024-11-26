package com.sabo.convention

import org.gradle.api.Project

fun Project.setNameSpace(name: String) {
    androidExtension.apply {
        namespace = "com.sabo.$name"
    }
}