plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}