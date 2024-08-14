plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.restaurant.restaurant"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.restaurant.restaurant"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.glide)
    implementation(libs.jackson.databind)
    testImplementation(libs.junit.junit)
    annotationProcessor (libs.compiler)
    implementation (libs.jackson.core)
    implementation (libs.jackson.databind.v2xy)



}