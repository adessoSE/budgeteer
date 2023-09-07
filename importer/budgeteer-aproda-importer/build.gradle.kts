plugins {
    id("de.adesso.budgeteer.importer-conventions")
}

dependencies {
    implementation(project(":budgeteer-importer-api"))
    implementation("org.apache.poi:poi-ooxml:${project.properties["poi_version"]}")
    runtimeOnly("commons-codec:commons-codec:${project.properties["commons_codec_version"]}")
}
