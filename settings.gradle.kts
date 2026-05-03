pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://maven.myket.ir")
        maven(url = "https://archive.ito.gov.ir/gradle/maven-central")
        maven(url = "https://mirror-maven.runflare.com/maven2")
        maven(url = "https://gradle.jamko.ir")
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven(url = "https://maven.myket.ir")
        maven(url = "https://archive.ito.gov.ir/gradle/maven-central")
        maven(url = "https://mirror-maven.runflare.com/maven2")
        maven(url = "https://gradle.jamko.ir")
        mavenCentral()
        google()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ComposeElasticOverscroll"

include(":app", ":overscroll")