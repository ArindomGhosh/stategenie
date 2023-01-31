pluginManagement {
    includeBuild("build_logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}
rootProject.name = "StateGenie"
include(":app")
include(":genie-annotations")
include(":genie-processors")
