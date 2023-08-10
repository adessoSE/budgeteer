plugins {
    java
    jacoco

    id("io.freefair.lombok")
    id("com.diffplug.spotless")
}
tasks {
    spotless {
        ratchetFrom(System.getProperty("mainBranch", "main"))

        java {
            googleJavaFormat()
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    test {
        finalizedBy(jacocoTestReport)
        useJUnitPlatform()
    }

    jacocoTestReport {
        reports {
            html.required.set(false)
            csv.required.set(false)
            xml.required.set(true)
        }
    }
}