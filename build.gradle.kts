plugins {
    id("org.sonarqube") version "4.3.0.3225"
}

allprojects {
    version = "1.1.4.BETA"
    group = "de.adesso"
}

subprojects {
    repositories {
        repositories {
            mavenCentral()
            maven {
                url = uri("https://oss.jfrog.org/artifactory/jcenter")
            }
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "adessoAG_budgeteer")
        property("sonar.organization", "adesso-ag")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.exclusions", "**/build.gradle.kts")
    }
}
