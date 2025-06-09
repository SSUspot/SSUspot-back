package com.ssuspot.sns.infrastructure.configs

import org.springframework.context.annotation.Configuration

/**
 * JPA 설정 클래스
 * QueryDSL 대신 Spring Data JPA의 EntityManager와 @Query 사용
 */
@Configuration
class JpaConfig {
    // QueryDSL JPAQueryFactory 제거
    // EntityManager는 CustomPostRepositoryImpl에서 직접 주입받아 사용
}