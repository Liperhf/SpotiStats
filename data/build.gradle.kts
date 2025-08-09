import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.example.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val localProperties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                buildConfigField(
                    "String",
                    "SPOTIFY_CLIENT_ID",
                    localProperties.getProperty("SPOTIFY_CLIENT_ID")
                )
                buildConfigField(
                    "String",
                    "SPOTIFY_CLIENT_SECRET",
                    localProperties.getProperty("SPOTIFY_CLIENT_SECRET")
                )
                buildConfigField(
                    "String",
                    "SPOTIFY_REDIRECT_URI",
                    localProperties.getProperty("SPOTIFY_REDIRECT_URI")
                )
            }
            debug {
                buildConfigField(
                    "String",
                    "SPOTIFY_CLIENT_ID",
                    localProperties.getProperty("SPOTIFY_CLIENT_ID")
                )
                buildConfigField(
                    "String",
                    "SPOTIFY_CLIENT_SECRET",
                    localProperties.getProperty("SPOTIFY_CLIENT_SECRET")
                )
                buildConfigField(
                    "String",
                    "SPOTIFY_REDIRECT_URI",
                    localProperties.getProperty("SPOTIFY_REDIRECT_URI")
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
        buildFeatures {
            buildConfig = true
            compose = true
            resValues = true
        }
    }

    dependencies {

        implementation(project(":domain"))
        //SwipeRefresh
        implementation(libs.google.accompanist.swiperefresh)

        //UCrop
        implementation(libs.yalantis.ucrop)


        implementation(libs.accompanist.systemuicontroller)
        implementation(libs.android.auth)

        // Core
        implementation(libs.androidx.core.ktx.v1120)
        implementation(libs.androidx.lifecycle.runtime.ktx.v270)
        implementation(libs.androidx.activity.compose.v182)

        // Compose
        implementation(platform(libs.androidx.compose.bom.v20240200))
        implementation(libs.coil.compose)
        implementation(libs.ui)
        implementation(libs.ui.graphics)
        implementation(libs.ui.tooling.preview)
        implementation(libs.material3)
        implementation(libs.androidx.navigation.compose)

        // Dagger Hilt
        implementation(libs.hilt.android)
        kapt(libs.hilt.compiler)
        implementation(libs.androidx.hilt.navigation.compose)

        // Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        kapt(libs.androidx.room.compiler)

        // Retrofit
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)


        // Coroutines
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.kotlinx.coroutines.play.services)

        // Testing
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit.v115)
        androidTestImplementation(libs.androidx.espresso.core.v351)
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.mockk)
        testImplementation(libs.mockito.kotlin)
        androidTestImplementation(libs.ui.test.junit4)
        debugImplementation(libs.ui.tooling)
        debugImplementation(libs.ui.test.manifest)
        testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
    }
}