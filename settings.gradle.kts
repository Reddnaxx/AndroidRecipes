pluginManagement {
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
    }
}

rootProject.name = "project"
include(":app")
include(":feature:recipes")
include(":core:configs")
include(":feature:profile")
include(":feature:auth")
include(":core:theme")
include(":core:auth-domain")
include(":core:auth-data")
include(":core:ui")
include(":core:recipe-domain")
include(":core:recipe-data")
include(":core:s3")
include(":core:network")
include(":core:local")
