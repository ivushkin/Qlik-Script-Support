plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "local.qlik"
version = "0.0.1"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3")
    // Сборка на базе IntelliJ IDEA Community той же платформы (233.*),
    // чтобы избежать проблем с pycharmPC дистрибутивом при скачивании через gradle-intellij-plugin.
    type.set("IC")
    plugins.set(emptyList())
}

tasks {
    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("233.*")
    }

    runIde {
        jvmArgs("-Xmx2g")
    }
}

kotlin {
    jvmToolchain(17)
}

