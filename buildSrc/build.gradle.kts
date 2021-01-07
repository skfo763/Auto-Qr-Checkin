plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

ext {
    
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(group = "io.github.rybalkinsd", name = "kohttp", version = "0.11.1")
    implementation(kotlin("gradle-plugin", "1.4.20"))

    implementation("com.google.guava:guava:28.2-jre")
    implementation("com.google.auth:google-auth-library-oauth2-http:0.20.0")
    implementation("com.google.apis:google-api-services-androidpublisher:v3-rev20191202-1.30.3")
    implementation("com.android.tools.build:gradle:4.0.2")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
}