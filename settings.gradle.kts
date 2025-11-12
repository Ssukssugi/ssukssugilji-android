pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "SsukssukDiary"
include(":app")
include(":feature:main")
include(":core:designsystem")
include(":feature:login")
include(":core:navigator")
include(":core:network")
include(":core:data")
include(":core:model")
include(":feature:signup")
include(":core:datastore")
include(":feature:home")
include(":feature:diary")
include(":feature:profile")
include(":feature:town")
