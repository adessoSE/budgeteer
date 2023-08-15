plugins {
    id("de.adesso.budgeteer.spring-conventions")
}

dependencies {
    implementation(project(":budgeteer-common"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("commons-codec:commons-codec:${project.properties["commons_codec_version"]}")
    implementation("org.joda:joda-money:${project.properties["joda_money_version"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
