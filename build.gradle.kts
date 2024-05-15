import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    `java-library`
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "com.wanted"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    configurations {
        all {
            // Logback 의존성 제외
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project("app-wanted-market-external-api") {
    dependencies {
        implementation(project(":domain"))
        implementation(project(":common"))

        implementation("org.springframework.boot:spring-boot-starter-log4j2")
        implementation("org.springframework.boot:spring-boot-starter-web")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = true
    jar.enabled = false
}

project("domain") {
    dependencies {
        implementation(project(":common"))

        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        runtimeOnly("com.h2database:h2")
    }


    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project("common") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}