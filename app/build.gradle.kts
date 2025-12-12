import java.lang.module.ModuleFinder.compose

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.example.sclad_salo"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.sclad_salo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }


    composeOptions{
        kotlinCompilerExtensionVersion = "1.5.1"

    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)

    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)

   // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)


// Retrofit & OkHttp
   implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

// Coil
 implementation(libs.coil.compose)

// ML Kit
    implementation(libs.mlkit.translate)

}