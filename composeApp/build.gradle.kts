import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("com.google.gms.google-services")

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            export("io.github.mirzemehdi:kmpnotifier:1.4.0")
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        iosMain.dependencies {
//            implementation("org.jetbrains.skiko:skiko-iossimulatorarm64:0.8.25.1")
//            implementation("org.jetbrains.skiko:skiko-iosarm64:0.8.25.1")
            implementation("dev.icerock.moko:permissions-camera:0.19.0")

        }

        iosSimulatorArm64Main.dependencies {
            implementation("org.jetbrains.skiko:skiko-iossimulatorarm64:0.8.25.1")
        }

        iosArm64Main.dependencies {
            implementation("org.jetbrains.skiko:skiko-iosarm64:0.8.25.1")
            implementation("dev.icerock.moko:permissions-compose:0.19.0")
        }


        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)

        }
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.accompanist.permissions)
            implementation("dev.icerock.moko:permissions-compose:0.19.0")
            implementation("dev.icerock.moko:permissions-camera:0.19.0")
            implementation(libs.firebase.cloud.messaging)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

            implementation("com.attafitamim.krop:ui:0.1.6")
            implementation("org.jetbrains.skiko:skiko:0.8.25.1")

            implementation(libs.decompose)
            implementation(libs.decompose.extensions.compose)

            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)


            api(libs.datastore.core)
            api(libs.datastore.prefernces)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.supabase.auth)
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.postgres)

            




            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
            api("io.coil-kt.coil3:coil-compose:3.0.4")
            api("io.github.mirzemehdi:kmpnotifier:1.4.0")

            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.5.1")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.serialization)

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

            // Enables FileKit with Composable utilities
            implementation("io.github.vinceglb:filekit-compose:0.8.8")
            implementation(libs.firebase.storage)
//            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.5.1-shaded-core")
//            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.5.1-shaded")
        }
        desktopMain.dependencies {
            implementation("dev.gitlive:firebase-java-sdk:0.4.8")
            implementation("io.ktor:ktor-client-java:3.0.3")
            implementation("org.jetbrains.skiko:skiko-awt:0.7.21")
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

        }
    }
}

android {
    namespace = "com.uspower"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.uspower"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 8
        versionName = "1.6.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
}

dependencies {
//    implementation(libs.firebase.appdistribution.gradle)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.example.uspower.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.uspower"
            packageVersion = "1.0.0"
        }
    }
}
