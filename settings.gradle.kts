enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VocaPop"

include(":shared")
include(":androidApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
