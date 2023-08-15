plugins {
    id("de.adesso.budgeteer.spring-conventions")
    war
}

dependencies {
    implementation(project(":budgeteer-importer-api"))
    implementation(project(":importer:budgeteer-aproda-importer"))
    implementation(project(":importer:budgeteer-ubw-importer"))
    implementation(project(":importer:budgeteer-resourceplan-importer"))
    implementation(project(":importer:budgeteer-powerbi-importer"))
    implementation(project(":budgeteer-report-exporter"))
    implementation(project(":budgeteer-core"))
    implementation(project(":budgeteer-common"))
    implementation(project(":budgeteer-persistence"))

    implementation("org.apache.poi:poi-ooxml:${project.properties["poi_version"]}")

    implementation("org.apache.wicket:wicket-spring:${project.properties["wicket_version"]}")
    implementation("org.apache.wicket:wicket-extensions:${project.properties["wicket_version"]}")
    implementation("org.wicketstuff:wicketstuff-lazymodel:${project.properties["wicket_version"]}")
    implementation("org.wicketstuff:wicketstuff-tinymce4:${project.properties["wicket_version"]}")
    implementation("org.wicketstuff:wicketstuff-lambda-components:${project.properties["wicket_version"]}")
    implementation("de.adesso.wicked-charts:wicked-charts-wicket8:${project.properties["wickedcharts_version"]}")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.reflections:reflections:${project.properties["reflections_version"]}")
    implementation("org.apache.commons:commons-lang3:${project.properties["commons_lang_version"]}")
    implementation("commons-codec:commons-codec:${project.properties["commons_codec_version"]}")

    implementation("org.joda:joda-money:${project.properties["joda_money_version"]}")
    implementation("de.jollyday:jollyday:${project.properties["jollyday_version"]}")

    testImplementation("org.kubek2k:springockito:${project.properties["springockito_version"]}")
    testImplementation("org.kubek2k:springockito-annotations:${project.properties["springockito_version"]}")
    testImplementation("com.github.springtestdbunit:spring-test-dbunit:${project.properties["spring_dbunit_version"]}")
    testImplementation("org.dbunit:dbunit:${project.properties["dbunit_version"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    processResources {
        from("src/main/java") {
            exclude("**/*.xlsx")
            filter {
                it.replace("@application.version@", project.version.toString())
            }
        }
    }

    jar {
       enabled = true
    }
}
