plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.15")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.2")
    implementation("io.freefair.gradle:lombok-plugin:6.0.0-m2")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.20.0")
}
