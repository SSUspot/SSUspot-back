import com.ewerk.gradle.plugins.tasks.QuerydslCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
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

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.hibernate.validator:hibernate-validator:6.0.21.Final")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// spring redis
	compile ("org.springframework.boot:spring-boot-starter-data-redis")
	compile ("org.springframework.data:spring-data-redis")
	compile (group = "redis.clients", name = "jedis", version = "2.9.0")
	compile (group = "it.ozimov", name = "embedded-redis", version = "0.7.1")

	// querydsl
	implementation("com.querydsl:querydsl-jpa:5.0.0")
	kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// amazon
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	implementation("com.amazonaws:aws-java-sdk-core:1.12.385")
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.385")

	// read metadata
	implementation("com.drewnoakes:metadata-extractor:2.16.0")

	// kafka streams
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	// prometheus
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}
tasks {
	val compileQuerydsl by getting
	val compileKotlin by getting {
		dependsOn(compileQuerydsl)
	}
}



tasks.withType<Test> {
	useJUnitPlatform()
}

val querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}
sourceSets.getByName("main") {
	java.srcDir(querydslDir)
}
configurations {
	named("querydsl") {
		extendsFrom(configurations.compileClasspath.get())
	}
}
tasks.withType<QuerydslCompile> {
	options.annotationProcessorPath = configurations.querydsl.get()
}