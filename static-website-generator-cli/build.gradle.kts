plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.4.0"
}

version = "0.1"
group = "com.staticwebgenerator"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation(project(":static-website-generator-core"))
    runtimeOnly("ch.qos.logback:logback-classic")
}


application {
    mainClass = "com.staticwebgenerator.cli.StaticWebsiteGeneratorCliCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}


micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.staticwebgenerator.cli.*")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


