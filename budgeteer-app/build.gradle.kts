plugins {
    id("de.adesso.budgeteer.spring-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":budgeteer-common"))
    implementation(project(":budgeteer-core"))
    implementation(project(":budgeteer-rest"))
    implementation(project(":budgeteer-web-interface"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
