repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT")
}
