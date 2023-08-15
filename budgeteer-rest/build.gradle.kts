plugins {
    id("de.adesso.budgeteer.spring-conventions")
}

dependencies {
    implementation(project(":budgeteer-common"))
    implementation(project(":budgeteer-core"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.joda:joda-money:${project.properties["joda_money_version"]}")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
tasks {
    bootRun {
        enabled = false
    }

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
