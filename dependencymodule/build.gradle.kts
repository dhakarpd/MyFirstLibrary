import com.android.build.gradle.LibraryExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    /*the maven-publish Gradle plugin, which is responsible for creating and configuring Maven
    publications (the .aar or .jar + metadata that Gradle can later download as a dependency).*/
    id ("maven-publish")
}

android {
    namespace = "com.github.dhakarpd.dependencymodule"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 36
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
}

/*
* UC - i provided the variable group as group = "com.github.piyushdhakar" but pushed the code
* to github repository "com.github.dhakarpd" then what will the jitpack repository have
* an artifact named with?
*
* Ans - JitPack does not use the group value inside your build.gradle to decide where your library
* is hosted.It uses your GitHub repository URL instead.
*
  However, the groupId inside your Gradle configuration determines what the Gradle dependency name
* will be in the consumer’s project — so if they don’t match, you’ll have a mismatch.
*
* The artifact JitPack produces will be accessible under your GitHub username, i.e.:
* com.github.dhakarpd:MyFirstLibrary:1.0.0
*
* */
group = "com.github.dhakarpd"
version = "1.0.4"


// --- Publishing config for JitPack ---
/*
    When JitPack or Gradle runs your publish task:

      1- It compiles your library into .aar

      2- It generates metadata: pom.xml, module.json

      3- It packages everything

      4- It uploads that bundle to the remote Maven repo (JitPack or elsewhere)
*
* */

/*Your code runs inside an afterEvaluate { ... } block because the Android plugin doesn’t expose
its components (components["release"]) until after the project is fully configured.*/
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = group.toString()
                artifactId = "MyFirstLibrary" // must match your GitHub repo name
                version = version.toString()

                /*
                This creates your **POM file (Project Object Model)** — a standard Maven metadata file (`pom.xml`) that describes your library.
                    When you publish, this file gets generated and tells others:
                    - what your library is,
                    - who made it,
                    - where its source lives, etc.
                */
                pom {
                    name.set("MyFirstLibrary")
                    description.set("A simple reusable Android library by Piyush Dhakar")
                    url.set("https://github.com/dhakarpd/MyFirstLibrary")


                    /*Maven repositories (especially Maven Central) require a license tag so
                        consumers know under what terms your library can be used.*/
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
