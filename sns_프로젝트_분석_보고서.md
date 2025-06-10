# SSUspot SNS 프로젝트 심층 분석 보고서

## 개요
이 문서는 프로그래밍 입문자가 작성한 SSUspot SNS 프로젝트의 코드를 심층 분석하여 발견된 문제점들을 정리한 보고서입니다.

## 1. 프로젝트 구조 및 의존성 관리 문제

### 1.1 의존성 관리 문제
- **오래된 Spring Boot 버전 사용** (2.7.7)
  - 최신 버전 대비 보안 취약점 존재 가능
  - 새로운 기능 및 성능 개선 사항 미적용
  
- **오래된 JWT 라이브러리** (0.10.5)
  - 알려진 보안 취약점 존재
  - 최신 버전(0.11.x 이상) 사용 권장
  
- **Deprecated된 Gradle 문법 사용**
  ```kotlin
  compile ("org.springframework.boot:spring-boot-starter-data-redis")
  ```
  - `compile` 대신 `implementation` 사용 필요
  
- **중복된 의존성 선언**
  - querydsl 관련 의존성이 중복 선언됨
  - jackson-module-kotlin이 두 번 선언됨

### 1.2 설정 파일 문제 (application.yml)
- **하드코딩된 민감 정보**
  ```yaml
  redis:
    password: qualifiedOption565  # 하드코딩된 비밀번호
  ```
  
- **프로덕션 환경에 부적절한 설정**
  ```yaml
  jpa:
    show-sql: true  # 프로덕션에서는 false 권장
    hibernate:
      ddl-auto: update  # 프로덕션에서는 validate 또는 none 권장
  ```

### 1.3 Docker 설정 문제
- **docker-compose.yml에 하드코딩된 비밀번호**
  - 데이터베이스, Redis, Grafana 등 모든 서비스의 비밀번호가 노출됨
  - 환경 변수나 시크릿 관리 도구 사용 필요

## 2. API 설계 및 컨트롤러 레이어 문제

### 2.1 REST API 설계 문제
- **일관되지 않은 URL 패턴**
  - `/api/posts/users/me` vs `/api/users/{userId}/posts` 
  - 리소스 중심의 일관된 URL 설계 필요
  
- **부적절한 HTTP 메서드 사용**
  ```kotlin
  @PostMapping("/api/users/following/{userId}")  // follow
  @DeleteMapping("/api/users/following/{userId}")  // unfollow
  ```
  - RESTful하지 않은 설계

### 2.2 컨트롤러 구현 문제
- **중복 코드**
  ```kotlin
  return ResponseEntity.ok(
      posts.map { it.toResponseDto() }
  )
  ```
  - 모든 컨트롤러에서 반복되는 패턴
  
- **페이지네이션 기본값 하드코딩**
  ```kotlin
  @RequestParam("page", defaultValue = "1") page: Int
  ```
  - 설정 파일로 관리 필요
  - 페이지가 1부터 시작 (일반적으로 0부터 시작)

### 2.3 응답 처리 문제
- **null safety 문제**
  ```kotlin
  user.id!!  // 강제 언래핑 사용
  ```
  - NullPointerException 위험
  
- **일관되지 않은 응답 형식**
  - DELETE 메서드에서 응답 본문 없음
  - 성공/실패 상태를 명확히 표시하지 않음

## 3. 서비스 레이어 비즈니스 로직 문제

### 3.1 트랜잭션 관리 문제
- **일관되지 않은 @Transactional 사용**
  - 일부 메서드만 트랜잭션 적용
  - 읽기 전용 트랜잭션 미구분
  
### 3.2 중복 코드 및 비효율적 구현
- **중복 메서드**
  ```kotlin
  fun findValidUserByEmail(email: String): User
  fun getValidUserByEmail(email: String): User  // 동일한 기능
  ```
  
- **비효율적인 에러 처리**
  ```kotlin
  try {
      applicationEventPublisher.publishEvent(registeredUserEvent)
  } catch (e: Exception) {
      println("event publish error")  // println 사용
  }
  ```

### 3.3 보안 문제
- **TODO 주석 방치**
  ```kotlin
  // TODO: refresh token 캐시에 업데이트 하는 코드 작성 필요
  ```
  - 미완성 기능이 프로덕션에 배포될 위험

## 4. 도메인 모델 및 엔티티 설계 문제

### 4.1 엔티티 설계 문제
- **양방향 연관관계 남용**
  ```kotlin
  @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
  val posts: MutableList<Post> = mutableListOf()
  ```
  - 모든 관계에 CascadeType.ALL 적용 (위험)
  - 순환 참조 가능성
  
- **부적절한 책임 분리**
  ```kotlin
  fun toDto(): PostResponseDto  // 엔티티에 DTO 변환 로직
  ```
  - 엔티티가 프레젠테이션 레이어에 의존

### 4.2 데이터 모델링 문제
- **FetchType.EAGER 사용**
  ```kotlin
  @ElementCollection(fetch = FetchType.EAGER)
  var imageUrls: List<String> = listOf()
  ```
  - N+1 쿼리 문제 발생 가능
  
- **불변성 문제**
  - var 사용으로 엔티티 필드 변경 가능
  - 데이터 무결성 위험

## 5. 데이터 접근 계층 문제

### 5.1 QueryDSL 사용 문제
- **N+1 쿼리 문제**
  ```kotlin
  val hasLiked = post.postLikes.any { it.user.id == user.id }
  ```
  - 각 포스트마다 좋아요 확인을 위한 추가 쿼리 발생
  
- **Deprecated API 사용**
  ```kotlin
  .fetchCount()  // QueryDSL 5.0에서 deprecated
  ```

### 5.2 리포지토리 구현 문제
- **중복 코드**
  - 각 find 메서드마다 유사한 패턴 반복
  - 공통 로직 추출 필요
  
- **트랜잭션 경계 문제**
  - 리포지토리에 @Transactional(readOnly = true) 선언
  - 서비스 레이어로 이동 필요

## 6. 보안 취약점

### 6.1 인증/인가 문제
- **모든 엔드포인트 공개**
  ```kotlin
  .authorizeHttpRequests {
      it.anyRequest().permitAll()
  }
  ```
  - 인증이 필요한 엔드포인트도 모두 공개됨
  
### 6.2 JWT 구현 문제
- **토큰 검증 부족**
  - 만료 시간 검증 누락
  - 토큰 무효화 메커니즘 없음
  
- **예외 처리 부재**
  ```kotlin
  getUserEmailFromToken(token)!!["email"] as String
  ```
  - 토큰 파싱 실패 시 앱 크래시

### 6.3 CORS 설정 문제
- **CORS 설정 미완성**
  ```kotlin
  .cors { }  // 빈 설정
  ```

## 7. 예외 처리 및 에러 핸들링 문제

### 7.1 일관되지 않은 HTTP 상태 코드
```kotlin
return ResponseEntity.status(404).body(
    ErrorResponse(
        HttpStatus.BAD_REQUEST,  // 404인데 400 반환
        errorCode,
        exception.message ?: ""
    )
)
```

### 7.2 에러 코드 관리 문제
- **중복된 에러 코드 체계**
  - ErrorCode와 ApiErrorCode 두 개의 enum 존재
  - 실제로는 하나만 사용됨
  
- **잘못된 에러 코드 매핑**
  ```kotlin
  @ExceptionHandler(TagNotFoundException::class)
  val errorCode: String = ErrorCode.NOT_EXISTS_POST.message  // 태그인데 포스트 에러
  ```

## 8. 테스트 및 문서화 부재

### 8.1 테스트 코드 부재
- **단위 테스트 없음**
  - 비즈니스 로직 검증 불가
  - 리팩토링 시 안정성 보장 불가
  
- **통합 테스트 없음**
  - API 동작 검증 불가
  - 엔드포인트 간 상호작용 테스트 부재

### 8.2 문서화 부재
- **API 문서 없음**
  - Swagger/OpenAPI 스펙 없음
  - README 파일 없음
  
- **코드 주석 부재**
  - 복잡한 비즈니스 로직 설명 없음
  - 메서드/클래스 용도 불명확

## 9. 성능 최적화 문제

### 9.1 쿼리 최적화 부재
- **페이징 시 전체 카운트 쿼리**
  ```kotlin
  val total = queryFactory.selectFrom(post)
      .where(post.user.`in`(followingUsers))
      .fetchCount()
  ```
  - 매 페이지 요청마다 전체 카운트 실행
  
### 9.2 캐싱 전략 부재
- **Redis 설정만 있고 실제 사용 없음**
  - 자주 조회되는 데이터 캐싱 필요
  - 캐시 무효화 전략 없음

## 10. 개선 제안 사항

### 10.1 즉시 개선 필요 (Critical)
1. **보안 설정 강화**
   - 인증/인가 적용
   - 민감 정보 환경 변수로 관리
   - JWT 토큰 검증 강화

2. **프로덕션 설정 수정**
   - hibernate.ddl-auto를 validate로 변경
   - show-sql을 false로 변경

3. **의존성 업데이트**
   - Spring Boot 3.x로 업그레이드
   - JWT 라이브러리 최신 버전 사용

### 10.2 중기 개선 사항 (Important)
1. **코드 품질 개선**
   - 중복 코드 제거
   - null safety 개선
   - 적절한 예외 처리

2. **성능 최적화**
   - N+1 쿼리 문제 해결
   - 적절한 인덱스 추가
   - 캐싱 전략 구현

3. **테스트 코드 작성**
   - 단위 테스트 작성
   - 통합 테스트 작성
   - 테스트 커버리지 목표 설정

### 10.3 장기 개선 사항 (Nice to have)
1. **아키텍처 개선**
   - 레이어드 아키텍처 명확화
   - DDD 원칙 적용 검토
   - 이벤트 드리븐 아키텍처 검토

2. **문서화**
   - API 문서 자동화 (Swagger)
   - 개발자 가이드 작성
   - 아키텍처 문서 작성

3. **모니터링 및 로깅**
   - 구조화된 로깅 적용
   - 메트릭 수집 강화
   - 알림 시스템 구축

## 결론
이 프로젝트는 프로그래밍 입문자가 작성한 것으로, 기본적인 기능은 구현되어 있으나 프로덕션 환경에서 사용하기에는 많은 개선이 필요합니다. 특히 보안, 성능, 코드 품질 측면에서 즉각적인 개선이 필요하며, 장기적으로는 아키텍처와 개발 프로세스 전반에 걸친 개선이 필요합니다.