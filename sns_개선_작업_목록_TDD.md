# SSUspot SNS 프로젝트 개선 작업 목록 (TDD 방식)

## 개요
이 문서는 SNS 프로젝트를 TDD(Test-Driven Development) 방식으로 개선하기 위한 작업 목록입니다. Spring Boot 3.2+ 업그레이드와 Spring Security 6.x 마이그레이션을 포함합니다.

## TDD 개발 프로세스
모든 작업은 다음 순서로 진행됩니다:
1. **Red**: 실패하는 테스트 작성
2. **Green**: 테스트를 통과하는 최소한의 코드 작성
3. **Refactor**: 코드 개선 (테스트는 계속 통과해야 함)

## Phase 0: 개발 환경 및 테스트 인프라 구축 (3일)

### 0.1 테스트 환경 설정
**우선순위: Critical | 예상 소요: 1일**

#### Tasks:
1. **테스트 의존성 추가**
   - [ ] JUnit 5, Mockito, AssertJ 설정
   - [ ] TestContainers 설정 (PostgreSQL, Redis)
   - [ ] RestAssured 또는 WebTestClient 설정
   - [ ] 테스트 프로파일 설정 (application-test.yml)

2. **테스트 유틸리티 생성**
   - [ ] 테스트 픽스처 빌더 패턴 구현
   - [ ] 테스트용 Security 설정
   - [ ] 공통 테스트 어노테이션 생성
   - [ ] 테스트 데이터 초기화 전략 수립

### 0.2 CI/CD 파이프라인 구축
**우선순위: Critical | 예상 소요: 2일**

#### Tasks:
1. **GitHub Actions 설정**
   - [ ] 테스트 자동 실행 워크플로우
   - [ ] 코드 커버리지 리포트 (JaCoCo)
   - [ ] SonarQube 연동
   - [ ] PR 시 테스트 필수 통과 설정

## Phase 1: Spring Boot 3.2+ 마이그레이션 (1주)

### 1.1 의존성 업그레이드 준비
**우선순위: Critical | 예상 소요: 2일**

#### TDD Tasks:
1. **호환성 테스트 작성**
   - [ ] 현재 버전에서 모든 기능의 통합 테스트 작성
   - [ ] 각 API 엔드포인트별 테스트 케이스 작성
   - [ ] 데이터베이스 연동 테스트 작성
   - [ ] 현재 동작을 문서화하는 테스트 스위트 완성

2. **의존성 분석**
   - [ ] Spring Boot 3.2와 호환되지 않는 라이브러리 식별
   - [ ] 대체 라이브러리 선정 및 테스트
   - [ ] 마이그레이션 위험 요소 문서화

### 1.2 Spring Boot 3.2 마이그레이션
**우선순위: Critical | 예상 소요: 3일**

#### TDD Tasks:
1. **단계별 마이그레이션**
   - [ ] Spring Boot 2.7.7 → 2.7.17 (패치 버전 업데이트)
   - [ ] 모든 테스트 통과 확인
   - [ ] Spring Boot 2.7.17 → 3.0.13 (메이저 버전 업데이트)
   - [ ] Jakarta 네임스페이스 마이그레이션 (`javax.*` → `jakarta.*`)
   - [ ] Spring Boot 3.0.13 → 3.2.x (최신 버전)
   - [ ] 각 단계마다 전체 테스트 스위트 실행

2. **Spring Security 6.x 마이그레이션**
   - [ ] Security 설정 관련 테스트 먼저 작성
   - [ ] `WebSecurityConfigurerAdapter` 제거 (deprecated)
   - [ ] `SecurityFilterChain` 빈 방식으로 변경
   - [ ] `authorizeRequests()` → `authorizeHttpRequests()` 변경
   - [ ] `antMatchers()` → `requestMatchers()` 변경
   - [ ] CORS 및 CSRF 설정 업데이트
   - [ ] 각 변경사항별 테스트 작성 및 통과

3. **JWT 라이브러리 업그레이드**
   - [ ] JWT 관련 모든 기능의 테스트 작성
   - [ ] io.jsonwebtoken 0.10.5 → 0.11.5 업그레이드
   - [ ] 새로운 API에 맞춰 코드 수정
   - [ ] 토큰 생성/검증 테스트 통과 확인

### 1.3 추가 의존성 정리
**우선순위: High | 예상 소요: 1일**

#### TDD Tasks:
1. **Gradle 설정 현대화**
   - [ ] 의존성 버전 카탈로그 도입 테스트
   - [ ] `compile` → `implementation` 전환
   - [ ] 중복 의존성 제거
   - [ ] 빌드 성공 테스트

## Phase 2: Security 강화 (TDD) (2주)

### 2.1 인증/인가 시스템 재구현
**우선순위: Critical | 예상 소요: 5일**

#### TDD Tasks:
1. **Security 테스트 작성**
   - [ ] 인증되지 않은 사용자 접근 차단 테스트
   - [ ] 잘못된 토큰으로 접근 시 401 응답 테스트
   - [ ] 권한 없는 리소스 접근 시 403 응답 테스트
   - [ ] 올바른 토큰으로 접근 시 성공 테스트

2. **Security Config 구현**
   ```kotlin
   // 테스트 먼저 작성
   @Test
   fun `인증되지 않은 사용자는 보호된 엔드포인트에 접근할 수 없다`() {
       // given
       val request = get("/api/posts")
       
       // when & then
       mockMvc.perform(request)
           .andExpect(status().isUnauthorized())
   }
   ```
   - [ ] 위 테스트를 통과하는 SecurityConfig 구현
   - [ ] 공개 엔드포인트 테스트 및 구현
   - [ ] 인증 필요 엔드포인트 테스트 및 구현

3. **JWT 토큰 검증 강화**
   - [ ] 만료된 토큰 검증 실패 테스트
   - [ ] 잘못된 서명 토큰 검증 실패 테스트
   - [ ] 토큰 블랙리스트 테스트
   - [ ] 각 테스트를 통과하는 구현

### 2.2 환경 변수 및 시크릿 관리
**우선순위: Critical | 예상 소요: 2일**

#### TDD Tasks:
1. **환경 변수 로딩 테스트**
   - [ ] 필수 환경 변수 누락 시 애플리케이션 시작 실패 테스트
   - [ ] 환경 변수 우선순위 테스트 (환경변수 > application.yml)
   - [ ] 프로파일별 설정 로딩 테스트

2. **설정 외부화 구현**
   - [ ] `@ConfigurationProperties` 클래스 생성
   - [ ] 유효성 검증 테스트
   - [ ] 설정값 주입 테스트

## Phase 3: API 리팩토링 (TDD) (2주)

### 3.1 REST API 표준화
**우선순위: High | 예상 소요: 4일**

#### TDD Tasks:
1. **API 스펙 테스트 작성**
   ```kotlin
   @Test
   fun `게시물 생성 API는 201 Created와 Location 헤더를 반환한다`() {
       // given
       val request = CreatePostRequest(
           title = "테스트 제목",
           content = "테스트 내용",
           imageUrls = listOf(),
           tags = listOf("tag1"),
           spotId = 1L
       )
       
       // when & then
       mockMvc.perform(
           post("/api/posts")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request))
               .with(jwt())
       )
       .andExpect(status().isCreated())
       .andExpect(header().exists("Location"))
   }
   ```
   - [ ] 각 API별 표준 응답 테스트 작성
   - [ ] 테스트를 통과하는 컨트롤러 구현

2. **공통 응답 래퍼 구현**
   - [ ] 성공 응답 래퍼 테스트 및 구현
   - [ ] 에러 응답 래퍼 테스트 및 구현
   - [ ] 페이지네이션 응답 테스트 및 구현

### 3.2 DTO와 엔티티 분리
**우선순위: High | 예상 소요: 3일**

#### TDD Tasks:
1. **Mapper 테스트 작성**
   ```kotlin
   @Test
   fun `Post 엔티티를 PostResponse DTO로 변환한다`() {
       // given
       val post = Post(
           id = 1L,
           title = "제목",
           content = "내용",
           user = testUser,
           spot = testSpot
       )
       
       // when
       val dto = postMapper.toResponse(post)
       
       // then
       assertThat(dto.id).isEqualTo(1L)
       assertThat(dto.title).isEqualTo("제목")
       assertThat(dto.content).isEqualTo("내용")
   }
   ```
   - [ ] 각 엔티티별 Mapper 테스트 작성
   - [ ] MapStruct 또는 수동 Mapper 구현
   - [ ] 엔티티에서 toDto() 메서드 제거

### 3.3 서비스 레이어 리팩토링
**우선순위: High | 예상 소요: 3일**

#### TDD Tasks:
1. **비즈니스 로직 테스트**
   ```kotlin
   @Test
   fun `자신이 작성하지 않은 게시물은 수정할 수 없다`() {
       // given
       val post = createPost(author = anotherUser)
       val updateRequest = UpdatePostRequest(
           title = "수정된 제목",
           content = "수정된 내용"
       )
       
       // when & then
       assertThrows<AccessPostWithNoAuthException> {
           postService.updatePost(post.id, updateRequest, currentUser.email)
       }
   }
   ```
   - [ ] 각 비즈니스 규칙별 테스트 작성
   - [ ] 테스트를 통과하는 서비스 구현

## Phase 4: 데이터 접근 계층 개선 (TDD) (1주)

### 4.1 리포지토리 최적화
**우선순위: Medium | 예상 소요: 3일**

#### TDD Tasks:
1. **쿼리 성능 테스트**
   ```kotlin
   @Test
   fun `팔로우한 사용자의 게시물 조회 시 N+1 문제가 발생하지 않는다`() {
       // given
       val user = createUserWithFollowing(10)
       createPostsForFollowedUsers(100)
       
       // when
       val query = monitoring.countQueries {
           postRepository.findPostsByFollowingUsers(user, pageable)
       }
       
       // then
       assertThat(query).isLessThanOrEqualTo(3) // 예상 쿼리 수
   }
   ```
   - [ ] N+1 문제 검증 테스트 작성
   - [ ] Fetch Join 또는 @EntityGraph 적용
   - [ ] 쿼리 최적화 확인

### 4.2 캐싱 구현
**우선순위: Medium | 예상 소요: 2일**

#### TDD Tasks:
1. **캐시 동작 테스트**
   ```kotlin
   @Test
   fun `동일한 사용자 정보를 조회하면 캐시에서 가져온다`() {
       // given
       val email = "test@example.com"
       
       // when
       val firstCall = userService.findByEmail(email)
       val secondCall = userService.findByEmail(email)
       
       // then
       verify(userRepository, times(1)).findByEmail(email)
       assertThat(firstCall).isEqualTo(secondCall)
   }
   ```
   - [ ] 캐시 히트/미스 테스트
   - [ ] 캐시 무효화 테스트
   - [ ] TTL 테스트

## Phase 5: 예외 처리 및 유효성 검증 (TDD) (1주)

### 5.1 글로벌 예외 처리
**우선순위: High | 예상 소요: 2일**

#### TDD Tasks:
1. **예외 처리 테스트**
   ```kotlin
   @Test
   fun `존재하지 않는 게시물 조회 시 404 응답을 반환한다`() {
       // given
       val nonExistentId = 999L
       
       // when & then
       mockMvc.perform(get("/api/posts/{id}", nonExistentId))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.errorCode").value("NOT_EXISTS_POST"))
           .andExpect(jsonPath("$.message").exists())
   }
   ```
   - [ ] 각 예외 상황별 테스트 작성
   - [ ] GlobalExceptionHandler 구현
   - [ ] 일관된 에러 응답 형식 적용

### 5.2 입력 유효성 검증
**우선순위: High | 예상 소요: 2일**

#### TDD Tasks:
1. **Bean Validation 테스트**
   ```kotlin
   @Test
   fun `게시물 제목이 비어있으면 400 에러를 반환한다`() {
       // given
       val invalidRequest = CreatePostRequest(
           title = "",  // 빈 제목
           content = "내용",
           imageUrls = listOf(),
           tags = listOf(),
           spotId = 1L
       )
       
       // when & then
       mockMvc.perform(
           post("/api/posts")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(invalidRequest))
       )
       .andExpect(status().isBadRequest())
       .andExpect(jsonPath("$.errors[0].field").value("title"))
   }
   ```
   - [ ] 각 DTO별 유효성 검증 테스트
   - [ ] Custom Validator 테스트 및 구현
   - [ ] 에러 메시지 표준화

## Phase 6: 통합 및 E2E 테스트 (1주)

### 6.1 API 통합 테스트
**우선순위: High | 예상 소요: 3일**

#### TDD Tasks:
1. **전체 플로우 테스트**
   ```kotlin
   @Test
   fun `사용자는 회원가입 후 로그인하여 게시물을 작성할 수 있다`() {
       // given: 회원가입
       val registerRequest = RegisterRequest(...)
       val registerResponse = registerUser(registerRequest)
       
       // when: 로그인
       val loginRequest = LoginRequest(...)
       val token = login(loginRequest)
       
       // then: 게시물 작성
       val postRequest = CreatePostRequest(...)
       val postResponse = createPost(postRequest, token)
       
       assertThat(postResponse.statusCode).isEqualTo(201)
   }
   ```
   - [ ] 주요 사용자 시나리오별 테스트 작성
   - [ ] 실제 데이터베이스 사용 (TestContainers)
   - [ ] 트랜잭션 롤백 없이 실제 플로우 검증

### 6.2 성능 테스트
**우선순위: Medium | 예상 소요: 2일**

#### TDD Tasks:
1. **부하 테스트**
   - [ ] JMeter 또는 Gatling 설정
   - [ ] API별 응답 시간 기준 설정
   - [ ] 동시 사용자 처리 능력 테스트
   - [ ] 메모리 누수 테스트

## 개발 진행 체크리스트

### 각 기능 개발 시 TDD 체크리스트
- [ ] **Red**: 실패하는 테스트를 먼저 작성했는가?
- [ ] **Green**: 테스트를 통과하는 최소한의 코드를 작성했는가?
- [ ] **Refactor**: 테스트가 통과하는 상태에서 코드를 개선했는가?
- [ ] 테스트 커버리지가 80% 이상인가?
- [ ] 통합 테스트를 작성했는가?
- [ ] 문서화 (JavaDoc/KDoc)를 작성했는가?

### Spring Boot 3.2 마이그레이션 체크리스트
- [ ] Jakarta 네임스페이스로 전환 완료
- [ ] Spring Security 6.x 설정 완료
- [ ] 모든 deprecated API 제거
- [ ] 새로운 설정 방식 적용
- [ ] 전체 테스트 스위트 통과

### 코드 리뷰 체크리스트
- [ ] 테스트가 명확하고 이해하기 쉬운가?
- [ ] 테스트가 비즈니스 요구사항을 잘 표현하는가?
- [ ] 프로덕션 코드가 테스트를 통과하기 위한 최소한인가?
- [ ] SOLID 원칙을 준수하는가?
- [ ] Spring Boot 3.2의 새로운 기능을 활용했는가?

## 예상 일정

| Phase | 내용 | 기간 | 비고 |
|-------|------|------|------|
| Phase 0 | 테스트 인프라 구축 | 3일 | 필수 선행 작업 |
| Phase 1 | Spring Boot 3.2 마이그레이션 | 1주 | Spring Security 6.x 포함 |
| Phase 2 | Security 강화 (TDD) | 2주 | 가장 중요한 부분 |
| Phase 3 | API 리팩토링 (TDD) | 2주 | 병렬 진행 가능 |
| Phase 4 | 데이터 접근 계층 개선 | 1주 | 성능 최적화 포함 |
| Phase 5 | 예외 처리 및 유효성 검증 | 1주 | 안정성 향상 |
| Phase 6 | 통합 및 E2E 테스트 | 1주 | 최종 검증 |

**총 예상 기간: 8-9주**

## 주의사항

1. **TDD 원칙 준수**
   - 테스트 없이는 절대 코드를 작성하지 않습니다
   - 테스트가 실패하는 것을 확인한 후 구현합니다
   - 한 번에 하나의 테스트만 작성합니다

2. **Spring Boot 3.2 마이그레이션**
   - 한 번에 모든 것을 바꾸지 않고 단계별로 진행합니다
   - 각 단계마다 모든 테스트가 통과하는지 확인합니다
   - 롤백 계획을 항상 준비합니다

3. **지속적 통합**
   - 매일 main 브랜치에 머지합니다
   - 기능 브랜치는 작게 유지합니다
   - PR은 테스트 통과 필수입니다