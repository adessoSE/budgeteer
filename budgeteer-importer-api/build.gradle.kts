plugins {
    id ("de.adesso.budgeteer.java-conventions")
}

dependencies {
    implementation("org.joda:joda-money:${project.properties["joda_money_version"]}")
    implementation("org.apache.poi:poi-ooxml:${project.properties["poi_version"]}")
}
