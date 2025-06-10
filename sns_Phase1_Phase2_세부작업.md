# SNS 프로젝트 Phase 1 & Phase 2 세부 작업 목록

## Phase 0: 테스트 인프라 구축 (필수 선행)

### 0.1 테스트 의존성 추가
**Subtasks:**
1. [ ] build.gradle.kts에 테스트 의존성 추가
   - JUnit 5 (Spring Boot 3.2에 포함)
   - Mockito & MockK for Kotlin
   - AssertJ
   - TestContainers (PostgreSQL, Redis, Kafka)
   - RestAssured 또는 WebTestClient
   - WireMock (외부 API 모킹)
   
2. [ ] 테스트 프로파일 설정 (application-test.yml)
   - 임베디드 H2 데이터베이스 설정
   - 테스트용 JWT secret
   - 로깅 레벨 설정
   
3. [ ] JaCoCo 코드 커버리지 설정
   - 최소 커버리지 80% 설정
   - 커버리지 리포트 생성

### 0.2 테스트 유틸리티 생성
**Subtasks:**
1. [ ] 테스트 픽스처 빌더 생성
   - UserFixture
   - PostFixture
   - CommentFixture
   - SpotFixture
   
2. [ ] 테스트용 어노테이션 생성
   - @IntegrationTest
   - @RepositoryTest
   - @ServiceTest
   - @WithMockUser (커스텀)
   
3. [ ] 테스트 헬퍼 유틸리티
   - JwtTestHelper
   - DatabaseCleanup
   - TestTransaction

### 0.3 CI/CD 파이프라인 구축
**Subtasks:**
1. [ ] GitHub Actions 워크플로우 생성
   - PR 시 자동 테스트 실행
   - 테스트 실패 시 머지 차단
   - 코드 커버리지 리포트 코멘트
   
2. [ ] SonarQube 또는 CodeClimate 연동
   - 코드 품질 분석
   - 기술 부채 추적
   - 보안 취약점 스캔

## Phase 1: Spring Boot 3.2+ 마이그레이션

### 1.1 현재 상태 테스트 작성
**Subtasks:**
1. [ ] API 엔드포인트 통합 테스트
   - 모든 Controller 엔드포인트 테스트
   - 현재 동작하는 Request/Response 문서화
   - 인증/인가 플로우 테스트
   
2. [ ] 서비스 레이어 단위 테스트
   - UserService 전체 메서드 테스트
   - PostService 전체 메서드 테스트
   - CommentService 전체 메서드 테스트
   
3. [ ] 리포지토리 레이어 테스트
   - QueryDSL 쿼리 동작 테스트
   - 커스텀 리포지토리 메서드 테스트
   - N+1 문제 검증 테스트

### 1.2 Spring Boot 2.7.7 → 2.7.17 업그레이드
**Subtasks:**
1. [ ] 패치 버전 업그레이드
   - build.gradle.kts 수정
   - 전체 테스트 실행 및 통과 확인
   - deprecated 경고 확인
   
2. [ ] 의존성 호환성 체크
   - Spring Boot 2.7.17 BOM 확인
   - 충돌하는 의존성 해결

### 1.3 Spring Boot 2.7.17 → 3.0.13 업그레이드
**Subtasks:**
1. [ ] Jakarta 네임스페이스 마이그레이션
   - javax.* → jakarta.* 일괄 변경
   - javax.persistence → jakarta.persistence
   - javax.validation → jakarta.validation
   - javax.servlet → jakarta.servlet
   
2. [ ] Spring Boot 3.0 breaking changes 대응
   - spring.config.use-legacy-processing 제거
   - spring.mvc.pathmatch.matching-strategy 변경
   - Actuator 엔드포인트 변경사항 적용
   
3. [ ] 의존성 업그레이드
   - Hibernate 6.x 대응
   - QueryDSL 5.x 설정 변경
   - Jackson 2.14+ 대응

### 1.4 Spring Boot 3.0.13 → 3.2.x 업그레이드
**Subtasks:**
1. [ ] 최신 버전 업그레이드
   - Spring Boot 3.2.x 적용
   - 새로운 기능 활용 검토
   - 성능 개선사항 확인
   
2. [ ] Kotlin 버전 업그레이드
   - Kotlin 1.8.22 → 1.9.x
   - Kotlin 컴파일러 옵션 최적화

### 1.5 Spring Security 6.x 마이그레이션
**Subtasks:**
1. [ ] SecurityConfig 재작성
   - WebSecurityConfigurerAdapter 제거
   - SecurityFilterChain Bean 방식으로 변경
   - Lambda DSL 적용
   
2. [ ] API 변경사항 적용
   - authorizeRequests() → authorizeHttpRequests()
   - antMatchers() → requestMatchers()
   - mvcMatchers() 제거
   
3. [ ] CORS/CSRF 설정 업데이트
   - 새로운 CORS 설정 방식 적용
   - CSRF 토큰 처리 업데이트

### 1.6 JWT 라이브러리 업그레이드
**Subtasks:**
1. [ ] io.jsonwebtoken 0.10.5 → 0.11.5
   - 새로운 API 적용
   - 보안 알고리즘 업데이트
   - 키 생성 방식 변경
   
2. [ ] JWT 관련 코드 리팩토링
   - Claims 파싱 로직 수정
   - 토큰 생성/검증 로직 업데이트
   - 예외 처리 강화

### 1.7 Gradle 설정 현대화
**Subtasks:**
1. [ ] Deprecated 문법 수정
   - compile → implementation
   - testCompile → testImplementation
   - runtime → runtimeOnly
   
2. [ ] 중복 의존성 제거
   - jackson-module-kotlin 중복 제거
   - querydsl 의존성 정리
   
3. [ ] 버전 카탈로그 도입 검토
   - gradle/libs.versions.toml 생성
   - 중앙집중식 버전 관리

## Phase 2: Security 강화 (TDD)

### 2.1 Security 테스트 작성
**Subtasks:**
1. [ ] 인증 테스트
   - 로그인 성공/실패 테스트
   - 토큰 발급 테스트
   - Refresh Token 테스트
   
2. [ ] 인가 테스트
   - 공개 엔드포인트 접근 테스트
   - 보호된 엔드포인트 접근 테스트
   - 본인 리소스만 수정 가능 테스트
   
3. [ ] 예외 상황 테스트
   - 만료된 토큰 테스트
   - 잘못된 형식의 토큰 테스트
   - 권한 없는 접근 테스트

### 2.2 Security Config 구현
**Subtasks:**
1. [ ] SecurityFilterChain 구현
   - 공개 엔드포인트 설정
   - 인증 필요 엔드포인트 설정
   - 역할 기반 접근 제어
   
2. [ ] JWT 필터 구현
   - JwtAuthenticationFilter 재작성
   - 토큰 파싱 및 검증
   - SecurityContext 설정
   
3. [ ] 예외 핸들러 구현
   - AuthenticationEntryPoint
   - AccessDeniedHandler
   - 표준화된 에러 응답

### 2.3 JWT 토큰 검증 강화
**Subtasks:**
1. [ ] 토큰 검증 로직 강화
   - 만료 시간 검증
   - 서명 검증
   - Claims 유효성 검증
   
2. [ ] 토큰 블랙리스트 구현
   - Redis 기반 블랙리스트
   - 로그아웃 시 토큰 무효화
   - TTL 설정
   
3. [ ] Refresh Token 구현
   - Refresh Token 발급
   - Access Token 갱신
   - Refresh Token Rotation

### 2.4 환경 변수 관리
**Subtasks:**
1. [ ] 민감 정보 외부화
   - application.yml 정리
   - 환경 변수 매핑
   - 기본값 설정
   
2. [ ] ConfigurationProperties 생성
   - JwtProperties
   - DatabaseProperties
   - AwsProperties
   - RedisProperties
   
3. [ ] 프로파일별 설정
   - application-dev.yml
   - application-prod.yml
   - application-test.yml
   
4. [ ] 시크릿 관리
   - .env.example 생성
   - docker-compose 환경 변수화
   - 비밀번호 정책 문서화

### 2.5 보안 정책 구현
**Subtasks:**
1. [ ] 비밀번호 정책
   - 최소 길이 및 복잡도
   - 비밀번호 암호화 강화
   - 비밀번호 변경 API
   
2. [ ] API Rate Limiting
   - IP 기반 제한
   - 사용자 기반 제한
   - 429 응답 구현
   
3. [ ] 입력 검증 강화
   - XSS 방지
   - SQL Injection 방지
   - 파일 업로드 검증

## 예상 일정 및 우선순위

### Week 1-2: Phase 0 + Phase 1 시작
- Day 1-3: 테스트 인프라 구축
- Day 4-5: 현재 상태 테스트 작성
- Day 6-10: Spring Boot 단계별 업그레이드

### Week 3: Phase 1 완료
- Day 11-12: Spring Security 6.x 마이그레이션
- Day 13: JWT 라이브러리 업그레이드
- Day 14: 전체 통합 테스트 및 안정화

### Week 4-5: Phase 2
- Day 15-17: Security 테스트 작성
- Day 18-20: Security Config 구현
- Day 21-23: JWT 토큰 검증 강화
- Day 24-25: 환경 변수 관리 및 마무리

## 체크포인트

### Phase 1 완료 기준
- [ ] 모든 테스트가 Spring Boot 3.2에서 통과
- [ ] Jakarta 네임스페이스 전환 완료
- [ ] Spring Security 6.x 동작 확인
- [ ] 성능 저하 없음 확인

### Phase 2 완료 기준
- [ ] 모든 보호된 엔드포인트가 인증 필요
- [ ] JWT 토큰 검증 강화 완료
- [ ] 환경 변수로 모든 민감 정보 관리
- [ ] 보안 테스트 100% 통과