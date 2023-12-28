plugins {
    kotlin("jvm") version "1.9.21"
    idea
    `maven-publish`
}

group = "com.github.fablesfantasyrp"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.google.guava:guava:33.0.0-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            artifactId = "caturix"
            from(components["kotlin"])
        }
    }
}
