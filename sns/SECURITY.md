# SSUSpot SNS 보안 설정 가이드

## 환경 변수 설정

### 1. 필수 환경 변수

프로젝트를 실행하기 전에 다음 환경 변수들을 설정해야 합니다:

```bash
# JWT 시크릿 키 (최소 32자 이상)
export SSUSPOT_JWT_SECRET="your-secure-jwt-secret-key"

# Database 설정
export SSUSPOT_SNS_DB_URL="jdbc:postgresql://localhost:5432/ssuspot_sns"
export SSUSPOT_SNS_DB_USERNAME="ssuspot"
export SSUSPOT_SNS_DB_PASSWORD="your-database-password"

# Redis 설정
export SSUSPOT_REDIS_HOST="localhost"
export SSUSPOT_REDIS_PORT="6379"
export SSUSPOT_REDIS_PASSWORD="your-redis-password"

# AWS S3 설정
export SSUSPOT_S3_BUCKET_NAME="your-bucket-name"
export SSUSPOT_S3_ACCESS_KEY="your-access-key"
export SSUSPOT_S3_SECRET_KEY="your-secret-key"
export SSUSPOT_S3_REGION="ap-northeast-2"
```

### 2. 시크릿 키 생성

안전한 시크릿 키를 생성하려면 다음 명령을 실행하세요:

```bash
# 프로젝트 빌드
./gradlew build

# 시크릿 키 생성기 실행
./gradlew run -PmainClass=com.ssuspot.sns.infrastructure.utils.SecretKeyGeneratorKt
```

또는 다음 명령으로 직접 생성할 수 있습니다:

```bash
# JWT 시크릿 키 생성 (32바이트)
openssl rand -base64 32

# 강력한 비밀번호 생성 (24바이트)
openssl rand -base64 24
```

### 3. 환경별 설정

#### 개발 환경
```bash
export SPRING_PROFILES_ACTIVE=dev
```

#### 프로덕션 환경
```bash
export SPRING_PROFILES_ACTIVE=prod
export SSUSPOT_CORS_ALLOWED_ORIGINS="https://app.ssuspot.com,https://www.ssuspot.com"
```

### 4. .env 파일 사용 (선택사항)

환경 변수 관리를 위해 `.env` 파일을 사용할 수 있습니다:

1. `.env.template` 파일을 `.env`로 복사
```bash
cp .env.template .env
```

2. `.env` 파일에 실제 값 입력

3. 애플리케이션 실행 시 환경 변수 로드
```bash
export $(cat .env | xargs) && ./gradlew bootRun
```

## 보안 체크리스트

- [ ] JWT 시크릿 키가 최소 32자 이상인가?
- [ ] 모든 비밀번호가 강력한가? (대소문자, 숫자, 특수문자 포함)
- [ ] `.env` 파일이 `.gitignore`에 포함되어 있는가?
- [ ] 프로덕션 환경에서 디버그 로그가 비활성화되어 있는가?
- [ ] CORS 설정이 프로덕션 도메인만 허용하는가?
- [ ] Redis 비밀번호가 설정되어 있는가?
- [ ] AWS 자격 증명이 최소 권한 원칙을 따르는가?

## 시크릿 관리 도구

프로덕션 환경에서는 다음 도구들을 사용하여 시크릿을 안전하게 관리하세요:

- **AWS Secrets Manager**: AWS 환경에서 시크릿 관리
- **HashiCorp Vault**: 엔터프라이즈급 시크릿 관리
- **Kubernetes Secrets**: K8s 환경에서 시크릿 관리
- **Docker Secrets**: Docker Swarm 환경에서 시크릿 관리

## 보안 모니터링

- JWT 토큰 만료 시간 모니터링
- 비정상적인 로그인 시도 감지
- API 요청 속도 제한 구현
- 보안 헤더 설정 확인

## 긴급 대응

시크릿이 노출된 경우:

1. 즉시 새로운 시크릿 키 생성
2. 모든 환경에서 키 업데이트
3. 기존 세션/토큰 무효화
4. 보안 감사 로그 확인
5. 영향받은 사용자에게 알림