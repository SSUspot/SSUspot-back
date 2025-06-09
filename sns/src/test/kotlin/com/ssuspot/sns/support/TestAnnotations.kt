package com.ssuspot.sns.support

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

/**
 * Integration test annotation for testing with full Spring context
 */
@Target(CLASS)
@Retention(RUNTIME)
@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
annotation class IntegrationTest

/**
 * Repository test annotation for testing JPA repositories
 */
@Target(CLASS)
@Retention(RUNTIME)
@Tag("repository")
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension::class)
annotation class RepositoryTest

/**
 * Service test annotation for testing service layer with mocked dependencies
 */
@Target(CLASS)
@Retention(RUNTIME)
@Tag("service")
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
annotation class ServiceTest

/**
 * Controller test annotation for testing REST controllers
 */
@Target(CLASS)
@Retention(RUNTIME)
@Tag("controller")
@ActiveProfiles("test")
@WebMvcTest
annotation class ControllerTest

/**
 * Unit test annotation for pure unit tests without Spring context
 */
@Target(CLASS)
@Retention(RUNTIME)
@Tag("unit")
annotation class UnitTest