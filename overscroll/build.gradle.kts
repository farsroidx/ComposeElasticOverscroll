plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose)
}

android {

    namespace = "ir.farsroidx.overscroll"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes { release { isMinifyEnabled = true } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {

    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll("-Xannotation-default-target=param-property")
    }
}

dependencies {

    // Compose
    implementation( platform( libs.compose.bom ) )
    implementation(libs.bundles.compose)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}