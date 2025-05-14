repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Version.compose
}

compose.desktop {
    application {
        mainClass = "org.kepocnhh.es.AppKt" // todo
        nativeDistributions.packageName = rootProject.name
    }
}

dependencies {
    project(":shared")
    implementation(compose.desktop.currentOs)
}
