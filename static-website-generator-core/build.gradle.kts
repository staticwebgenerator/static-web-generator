plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(platform(libs.micronaut.platform))
    implementation(libs.micronaut.views.thymeleaf)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
tasks.withType<Test> {
    useJUnitPlatform()
}