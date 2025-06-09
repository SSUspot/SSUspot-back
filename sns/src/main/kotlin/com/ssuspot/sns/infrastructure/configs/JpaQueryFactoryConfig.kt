package com.ssuspot.sns.infrastructure.configs

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Configuration
class JpaQueryFactoryConfig {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    @Bean
    fun jpaQueryFactory() =
        JPAQueryFactory(entityManager)
}