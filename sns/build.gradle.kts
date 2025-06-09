import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	// id("com.ewerk.gradle.plugins.querydsl") version "1.0.10" // Removed - using annotation processor instead
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"
	// kotlin("kapt") version "1.9.20" // Removed due to Java 21 compatibility issues
	id("jacoco")
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
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Database
	implementation("org.postgresql:postgresql")
	runtimeOnly("com.h2database:h2")
	
	// AWS SDK
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.261")

	// QueryDSL for Jakarta
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Redis (deprecated compile -> implementation)
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.data:spring-data-redis")
	implementation(group = "redis.clients", name = "jedis", version = "2.9.0")
	testImplementation(group = "it.ozimov", name = "embedded-redis", version = "0.7.1")

	// AWS SDK v2
	implementation("software.amazon.awssdk:s3:2.21.0")
	implementation("software.amazon.awssdk:auth:2.21.0")

	// Kafka
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	// Monitoring
	implementation("io.micrometer:micrometer-registry-prometheus")

	// Others
	implementation("com.drewnoakes:metadata-extractor:2.16.0")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.security:spring-security-test")
	
	// Enhanced test dependencies for TDD
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("io.rest-assured:rest-assured:5.3.2")
	testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
	
	// TestContainers
	testImplementation("org.testcontainers:testcontainers:1.19.3")
	testImplementation("org.testcontainers:junit-jupiter:1.19.3")
	testImplementation("org.testcontainers:postgresql:1.19.3")
	testImplementation("org.testcontainers:kafka:1.19.3")
	testImplementation("com.redis.testcontainers:testcontainers-redis-junit-jupiter:1.4.6")
	
	// WireMock for external API mocking
	testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:3.0.1")
	
	// Kotest (optional - popular Kotlin test framework)
	testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
	testImplementation("io.kotest:kotest-assertions-core:5.8.0")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

// Kapt configuration removed due to Java 17+ compatibility issues
// Using annotationProcessor instead
// Task dependencies removed since we're using annotationProcessor instead of kapt



tasks.withType<Test> {
	useJUnitPlatform()
}

// QueryDSL configuration
val querydslDir = "$buildDir/generated/sources/annotationProcessor/java/main"

sourceSets {
	main {
		java.srcDir(querydslDir)
	}
}


// JaCoCo Configuration
jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.required.set(true)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
	
	classDirectories.setFrom(
		files(classDirectories.files.map {
			fileTree(it) {
				exclude(
					"**/Q*",
					"**/*Application*",
					"**/config/**",
					"**/dto/**",
					"**/entity/**",
					"**/exception/**"
				)
			}
		})
	)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.80".toBigDecimal()
			}
		}
		
		rule {
			enabled = true
			element = "CLASS"
			includes = listOf("com.ssuspot.sns.application.service.*")
			
			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = "0.80".toBigDecimal()
			}
		}
	}
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}