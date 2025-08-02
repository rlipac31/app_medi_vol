plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // ESTA LÍNEA ES FUNDAMENTAL:
    alias(libs.plugins.kotlin.parcelize)

}

android {
    namespace = "com.example.medivol_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.medivol_1"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/INDEX.LIST",
                "META-INF/io.netty.versions.properties",
                "META-INF/DEPENDENCIES"
            )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}



dependencies {
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")// Versión actual a Julio 2025
    implementation("com.squareup.retrofit2:converter-gson:2.12.0") // Debe coincidir con la versión de Retrofit
 
    //
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.cardview:cardview:1.0.0") // Para CardView
    implementation ("androidx.recyclerview:recyclerview:1.3.2") // Para RecyclerView
    implementation ("com.google.android.material:material:1.12.0") // Para FloatingActionButton y Material Buttons
    implementation ("androidx.appcompat:appcompat:1.7.0") // Para Toolbar
    // OkHttp logging interceptor (para ver las llamadas de red en Logcat)
   // implementation("com.squareup.okhttp3:okhttp:5.0.0")// da error
   // implementation("com.squareup.okhttp3:logging-interceptor:5.0.0")// da error
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //para leer archivos json
    implementation("com.google.code.gson:gson:2.10.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.appdistribution.gradle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}