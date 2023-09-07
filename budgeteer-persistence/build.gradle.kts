plugins {
    id("de.adesso.budgeteer.spring-conventions")
}

dependencies {
    implementation(project(":budgeteer-common"))
    implementation(project(":budgeteer-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("commons-codec:commons-codec:${project.properties["commons_codec_version"]}")
    implementation("org.apache.poi:poi-ooxml:5.2.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.joda:joda-money:${project.properties["joda_money_version"]}")

    runtimeOnly("org.hsqldb:hsqldb:${project.properties["hsqldb_version"]}")
    runtimeOnly("org.postgresql:postgresql:${project.properties["postgresql_version"]}")

    testImplementation("com.github.springtestdbunit:spring-test-dbunit:${project.properties["spring_dbunit_version"]}")
    testImplementation("org.dbunit:dbunit:${project.properties["dbunit_version"]}")
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
