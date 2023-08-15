plugins {
    id("de.adesso.budgeteer.java-conventions")
}

dependencies {
    implementation(project(":budgeteer-importer-api"))
    implementation("org.apache.poi:poi-ooxml:${project.properties["poi_version"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.properties["junit5_version"]}")
    testImplementation("org.mockito:mockito-core:${project.properties["mockito_version"]}")
    testImplementation("org.assertj:assertj-core:${project.properties["assertj_version"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${project.properties["junit5_version"]}")
}
