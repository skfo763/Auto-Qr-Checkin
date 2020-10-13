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
    implementation("com.android.tools.build:gradle:4.0.1")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
    implementation(kotlin("gradle-plugin", "1.4.0"))
}