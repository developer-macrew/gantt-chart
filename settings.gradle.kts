
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven (url = "https://jitpack.io" )
        google()
        mavenCentral()
        mavenLocal()
    }

}

rootProject.name = "GanttChartLibrary"
include(":app")
include(":GanttChart")
