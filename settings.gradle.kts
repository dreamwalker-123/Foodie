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
        // добавить эту строчку
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

rootProject.name = "Foodie"
include(":app")
include(":feature:catalog")
include(":feature:card_product")
include(":feature:basket")
include(":feature:splash")
include(":core:data")
include(":core:ui")
include(":core:runtime")
include(":core:network")
include(":core:model")
