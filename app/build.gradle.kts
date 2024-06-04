plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltAndroid)
}

android {
    namespace = "com.example.foodie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.foodie.utilities.MainTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":feature:catalog"))
    implementation(project(":feature:card_product"))
    implementation(project(":feature:basket"))
    implementation(project(":feature:splash"))

    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:runtime"))

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowSizeClass)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // DI with hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.runner)
    ksp(libs.hilt.android.compiler)

    // Test
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // for TestNavHostController
    androidTestImplementation(libs.androidx.navigation.testing)

    // Hilt Testing for instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    // ...with Kotlin.
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}