plugins {
    java
    application
    id("me.qoomon.git-versioning") version "6.4.4"
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
    id("io.freefair.lombok") version "8.11"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
}

group = "io.github.alkoleft"
version = "1.0-SNAPSHOT"

gitVersioning.apply {
    refs {
        considerTagsOnBranches = true
        tag("v(?<tagVersion>[0-9].*)") {
            version = "\${ref.tagVersion}\${dirty}"
        }
        branch(".+") {
            version = "\${ref}-\${commit.short}\${dirty}"
        }
    }

    rev {
        version = "\${commit.short}\${dirty}"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://jitpack.io")
}
val JACKSON_VERSION = "2.15.2"
val JUINT_VERSION = "5.9.1"

dependencies {
    // CLI
    implementation("info.picocli", "picocli", "4.7.5")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("info.picocli:picocli-spring-boot-starter:4.7.6")

    // HBK
    implementation("com.github._1c_syntax.bsl:bsl-context:1.0-SNAPSHOT")
    implementation("com.github.1c-syntax:bsl-help-toc-parser:ab6c83315d"){
        exclude("com.tunnelvisionlabs", "antlr4-annotations")
        exclude("com.ibm.icu", "*")
        exclude("org.antlr", "ST4")
        exclude("org.abego.treelayout", "org.abego.treelayout.core")
        exclude("org.antlr", "antlr-runtime")
    }

    // BSL
    implementation("io.github.1c-syntax", "mdclasses", "0.15.0-rc.1")
    implementation("io.github.1c-syntax", "utils", "0.6.1")
    implementation("io.github.1c-syntax", "bsl-common-library", "0.7.0")
    implementation("io.github.1c-syntax", "bsl-parser-core", "0.1.0")
    implementation("io.github.1c-syntax", "bsl-parser", "0.24.0") {
        exclude("com.tunnelvisionlabs", "antlr4-annotations")
        exclude("com.ibm.icu", "*")
        exclude("org.antlr", "ST4")
        exclude("org.abego.treelayout", "org.abego.treelayout.core")
        exclude("org.antlr", "antlr-runtime")
    }

    // JSON
    implementation("com.fasterxml.jackson.core:jackson-core:$JACKSON_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-databind:$JACKSON_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$JACKSON_VERSION")

    // Logging
    implementation("org.slf4j", "slf4j-api", "1.7.30")

    // Tests
    testImplementation("org.slf4j", "slf4j-log4j12", "1.7.30")
    testImplementation("org.junit.jupiter:junit-jupiter:$JUINT_VERSION")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$JUINT_VERSION")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$JUINT_VERSION")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standard_error")
    }
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

publishing {
    repositories {
        maven {
            name = "monaco-bsl-context"
            url = uri("https://maven.pkg.github.com/alkoleft/monaco-bsl-context")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
