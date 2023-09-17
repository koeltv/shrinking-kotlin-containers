@file:Suppress("INACCESSIBLE_TYPE")

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id("org.beryx.runtime") version "1.13.0"
    id("org.graalvm.buildtools.native") version "0.9.8"
}

group = "com.koeltv"
version = "0.0.1"

application {
    mainClass.set("com.koeltv.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

val jreVersion: String by project
val downloadPage: String by project
val jlinkOptions: String by project
val baseModules: String by project
val targets: String by project

runtime {
    options.set(jlinkOptions.split(',').map { it.trim() })
    modules.set(baseModules.split(',').map { it.trim() })
    targets.split(',').map { it.trim() }.run {
        filter { target -> project.hasProperty(target) || none { project.hasProperty(it) } }
            .forEach {
                val format = if (it == "windows") "zip" else "tar.gz"
                val encodedJreVersion = jreVersion.replace("_", "%2B")
                val link = "$downloadPage/jdk-$encodedJreVersion/OpenJDK17U-jdk_x64_${it}_hotspot_$jreVersion.$format"
                targetPlatform(it) { setJdkHome(jdkDownload(link)) }
            }
    }
}

graalvmNative {
    binaries {
        named("main") {
            verbose.set(true)

            // If we want a fully static executable
            if (hasProperty("static")) {
                buildArgs.add("--static")
                buildArgs.add("--libc=musl")
            } else if (hasProperty("mostly-static")) {
                buildArgs.add("-H:+StaticExecutableWithDynamicLibC")
            }

            buildArgs.add("--initialize-at-build-time=io.ktor,kotlin")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")

            buildArgs.add("--trace-class-initialization=ch.qos.logback.classic.Logger")
            buildArgs.add("--trace-object-instantiation=ch.qos.logback.core.AsyncAppenderBase\$Worker")
            buildArgs.add("--initialize-at-build-time=org.slf4j.LoggerFactory,ch.qos.logback,org.slf4j.impl.StaticLoggerBinder")

            imageName.set(project.name)
        }
    }
}