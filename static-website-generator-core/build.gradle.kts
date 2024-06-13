plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    annotationProcessor(platform(libs.micronaut.platform))
    annotationProcessor(libs.micronaut.inject.java)
    implementation(platform(libs.micronaut.platform))
    implementation(libs.micronaut.views.thymeleaf)
    implementation(libs.flexmark)
    implementation(libs.flexmark.ext.tables)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
tasks.withType<Test> {
    useJUnitPlatform()
}