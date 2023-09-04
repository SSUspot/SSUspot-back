import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "com.ssuspot"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}
dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	implementation("io.jsonwebtoken:jjwt-api:0.10.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.10.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.10.5")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.hibernate.validator:hibernate-validator:6.0.21.Final")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	runtimeOnly("com.h2database:h2")

}
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
