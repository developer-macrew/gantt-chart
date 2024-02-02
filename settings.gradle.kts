
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

        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            /** Configure path of your package repository on Github
             ** Replace GITHUB_USERID with your/organisation Github userID
             ** and REPOSITORY with the repository name on GitHub
             */
            url = uri("https://maven.pkg.github.com/GITHUB_USERID/REPOSITORY")
          /*  credentials {
                *//** Create github.properties in root project folder file with
                 ** gpr.usr=GITHUB_USER_ID & gpr.key=PERSONAL_ACCESS_TOKEN
                 ** Set env variable GPR_USER & GPR_API_KEY if not adding a properties file**//*

                username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
                password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
            }*/
        }
    }
}

rootProject.name = "GanttChartLibrary"
include(":app")
include(":GanttChart")
