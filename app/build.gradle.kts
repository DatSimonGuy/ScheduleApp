import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.example.scheduleapp"
    compileSdk = 36

    defaultConfig {
        val properties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) {
                load(file.inputStream())
            }
        }
        val apiServerUrl = properties.getProperty("API_SERVER_URL") ?: "https://default.example.com"

        buildTypes {
            release {
                buildConfigField("String", "API_SERVER_URL", "\"$apiServerUrl\"")
            }
        }

        applicationId = "com.example.scheduleapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 5002
        versionName = "0.5.2"

        buildConfigField(
            type = "String",
            name = "API_SERVER_URL",
            value = apiServerUrl
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            if (System.getenv("RELEASE_STORE_FILE") != null) {
                storeFile = file(System.getenv("RELEASE_STORE_FILE"))
                storePassword = System.getenv("RELEASE_STORE_PASSWORD")
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (System.getenv("RELEASE_STORE_FILE") != null) {
                signingConfig = signingConfigs["release"]
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl

            val type = variant.buildType.name
            var apkName = "ScheduleApp_${variant.versionName}"

            if (type != "release") {
                apkName += "-$type"
            }

            output.outputFileName = "$apkName.apk"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-cio:3.4.0")
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}