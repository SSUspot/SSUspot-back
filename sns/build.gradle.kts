import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("kapt") version "1.8.22"
}

group = "com.ssuspot"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation("com.querydsl:querydsl-jpa")

	// h2 for test
	runtimeOnly("com.h2database:h2")

	// postgreSQL
	implementation("org.postgresql:postgresql")

	// spring security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")

	implementation("io.jsonwebtoken:jjwt-api:0.10.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.10.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.10.5")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.hibernate.validator:hibernate-validator:6.0.21.Final")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// spring cache
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// querydsl
	implementation("com.querydsl:querydsl-jpa:4.4.0")
	kapt("com.querydsl:querydsl-apt:4.4.0:jpa")
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
