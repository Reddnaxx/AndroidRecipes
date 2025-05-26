plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.recipes"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":core:theme"))
    implementation(project(":core:ui"))
    implementation(project(":core:configs"))
    implementation(project(":core:recipe-domain"))
    implementation(project(":core:recipe-data"))
    implementation(project(":core:s3"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    ksp(libs.hilt.android.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}