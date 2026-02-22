plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.recetas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recetas"
        minSdk = 23
        targetSdk = 35
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

    // Configuración para pruebas con Robolectric
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
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

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Animaciones
    implementation("androidx.compose.animation:animation:1.5.4")
    implementation("androidx.compose.animation:animation-graphics:1.5.4")
    implementation("androidx.compose.animation:animation-core:1.5.4")

    // Lottie para animaciones vectoriales
    implementation("com.airbnb.android:lottie-compose:6.1.0")

    // Accompanist
    implementation("com.google.accompanist:accompanist-placeholder-material:0.32.0")

    // Palette para colores adaptativos
    implementation("androidx.palette:palette-ktx:1.0.0")

    // Coil para carga de imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ==================== FIREBASE ====================
    // Firebase BOM - maneja versiones automáticamente
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Firebase Authentication KTX
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firebase Firestore KTX
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Firebase Core KTX (extensiones Kotlin)
    implementation("com.google.firebase:firebase-common-ktx")
    // ==================================================

    // ==================== PRUEBAS UNITARIAS ====================
    // JUnit 4 - framework base de pruebas
    testImplementation(libs.junit)

    // Mockito - simulación de objetos (mocks)
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

    // Robolectric - pruebas con contexto Android sin emulador
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test:runner:1.5.2")
    testImplementation("androidx.test.ext:junit:1.1.5")

    // Coroutines test support
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // ==================== PRUEBAS INSTRUMENTADAS ====================
    // JUnit para Android
    androidTestImplementation(libs.androidx.junit)

    // Espresso - pruebas de interfaz de usuario
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // AndroidX Test
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Compose Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
