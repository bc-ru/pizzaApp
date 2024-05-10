plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
    id("kotlin-kapt")

}

android {
    namespace = "com.example.pizzaapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pizzaapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    kapt("androidx.room:room-compiler:2.6.1")

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
//    implementation(libs.room.compiler)
    implementation(libs.lifecycle.viewmodel.compose)
//    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // Google maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google maps for compose
    implementation("com.google.maps.android:maps-compose:2.8.0")
    // KTX for the Maps SDK for Android
    implementation("com.google.maps.android:maps-ktx:3.2.1")
    // KTX for the Maps SDK for Android Utility Library
    implementation("com.google.maps.android:maps-utils-ktx:3.2.1")

    implementation("io.coil-kt:coil-compose:2.0.0-rc01")

//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
//    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
//
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("androidx.appcompat:appcompat:1.3.1")
//    implementation("androidx.compose.runtime:runtime-livedata:2.7.7")
    implementation(libs.androidx.compose.runtime)



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}