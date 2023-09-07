plugins {
    id("de.adesso.budgeteer.importer-conventions")
}

dependencies {
    implementation(project(":budgeteer-importer-api"))
    implementation("org.apache.poi:poi:${project.properties["poi_version"]}")
    implementation("org.apache.poi:poi-ooxml:${project.properties["poi_version"]}")
}
