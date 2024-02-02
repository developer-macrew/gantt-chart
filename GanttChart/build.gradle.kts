plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("maven-publish")
}
group = "com.github.developer-macrew"
/*afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.jitpack"
                artifactId = "gantt-chart"
                version = "1.0.5"
                from(components["release"])
            }
// Creates a Maven publication called "release".

        }
    }
}*/


android {
    namespace = "com.chart.ganttchart"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    /*implementation ("com.github.jitpack:gantt-chart:1.0.4")*/

}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.chart.ganttchart"
            artifactId = "gantt-chart"
            version = " 1.0.8.6"
            afterEvaluate {
                from(components["release"])
                /*tasks.register<Zip>("generateRepo") {
                    val publishTask = tasks.named(
                        "publishReleasePublicationToMyrepoRepository",
                        PublishToMavenRepository::class.java)
                    from(publishTask.map { it.repository.url })
                    into("mylibrary")
                    archiveFileName.set("mylibrary.zip")
                }*/
            }
        }
    }
    repositories {
        maven {
            name = "gantt-chart"
            url = uri("${project.buildDir}/repo")
        }

    }

}