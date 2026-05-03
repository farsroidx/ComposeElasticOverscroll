plugins {
    // android
    alias(libs.plugins.android.application)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose)
}

android {

    namespace = "ir.farsroidx.app"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "ir.farsroidx.ceo"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters.addAll(
            listOf("fa", "en")
        )
    }

    buildTypes {

        debug {
            isDebuggable    = true
            isMinifyEnabled = false
        }

        release {

            isDebuggable    = false
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

kotlin {jvmToolchain(17) }

dependencies {

    // Module
    implementation(projects.overscroll)

    // Androidx
    implementation(libs.bundles.androidx)

    // Material
    implementation(libs.google.android.material)

    // Compose
    implementation( platform( libs.compose.bom ) )
    implementation(libs.bundles.compose)
    debugApi(libs.compose.ui.tooling)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}