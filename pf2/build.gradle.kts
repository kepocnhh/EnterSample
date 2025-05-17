import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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

compose.desktop {
    application {
        mainClass = "org.kepocnhh.es.MainKt" // todo
        nativeDistributions {
            packageName = "${rootProject.name}-${project.name}"
            packageVersion = "1.0.0"
            targetFormats(TargetFormat.Dmg)
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
    implementation("com.github.kepocnhh:Bytes:0.2.1-SNAPSHOT")
}
