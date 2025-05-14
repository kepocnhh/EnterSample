repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

dependencies {
    implementation(compose.desktop.currentOs)
}
