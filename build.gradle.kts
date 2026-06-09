plugins {
    id("java")
}

group = "edu.curso"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.8")
}

tasks.test {
    useJUnitPlatform()
}