plugins {
    id("org.sonarqube") version "3.3"
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

sonarqube {
    properties {
        property("sonar.projectKey", "adessoAG_budgeteer")
        property("sonar.organization", "adesso-ag")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
