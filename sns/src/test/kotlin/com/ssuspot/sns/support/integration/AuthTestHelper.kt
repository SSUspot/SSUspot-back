package com.ssuspot.sns.support.integration

import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.springframework.stereotype.Component

/**
 * 인증 관련 테스트 헬퍼
 * 회원가입, 로그인, JWT 토큰 발급 등을 편리하게 처리
 */
@Component
class AuthTestHelper {

    /**
     * 테스트용 사용자 등록 및 로그인하여 JWT 토큰 반환
     */
    fun registerAndLoginUser(
        email: String = "test@example.com",
        password: String = "password123",
        userName: String = "testuser",
        nickname: String = "테스트유저"
    ): String {
        // 회원가입
        registerUser(email, password, userName, nickname)
        
        // 로그인하여 토큰 획득
        return loginUser(email, password)
    }

    /**
     * 사용자 등록
     */
    fun registerUser(
        email: String,
        password: String,
        userName: String,
        nickname: String,
        profileMessage: String? = "테스트 프로필 메시지",
        profileImageLink: String? = null
    ) {
        val registerRequest = RegisterRequest(
            email = email,
            password = password,
            userName = userName,
            nickname = nickname,
            profileMessage = profileMessage,
            profileImageLink = profileImageLink
        )

        given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
        .`when`()
            .post("/api/users/register")
        .then()
            .statusCode(200)
    }

    /**
     * 사용자 로그인하여 Access Token 반환
     */
    fun loginUser(email: String, password: String): String {
        val loginRequest = LoginRequest(email, password)

        val response = given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
        .`when`()
            .post("/api/users/login")
        .then()
            .statusCode(200)
            .extract()
            .`as`(LoginResponse::class.java)

        return response.accessToken
    }

    /**
     * JWT 토큰을 Authorization 헤더 형태로 반환
     */
    fun bearerToken(token: String): String {
        return "Bearer $token"
    }

    /**
     * 다수의 테스트 사용자 생성 (팔로우, 게시물 등 테스트용)
     */
    fun createMultipleUsers(count: Int): List<Pair<String, String>> {
        return (1..count).map { index ->
            val email = "user$index@example.com"
            val password = "password$index"
            val userName = "user$index"
            val nickname = "사용자$index"
            
            val token = registerAndLoginUser(email, password, userName, nickname)
            Pair(email, token)
        }
    }

    /**
     * 관리자 사용자 생성 (권한 테스트용)
     */
    fun createAdminUser(): String {
        return registerAndLoginUser(
            email = "admin@example.com",
            password = "admin123",
            userName = "admin",
            nickname = "관리자"
        )
    }

    /**
     * 만료된 토큰 생성 (에러 케이스 테스트용)
     */
    fun createExpiredToken(): String {
        // 실제로는 JwtTokenProvider를 직접 사용하여 만료된 토큰 생성
        // 현재는 더미 값 반환 (실제 구현에서는 테스트용 토큰 생성 로직 필요)
        return "expired.jwt.token"
    }

    /**
     * 잘못된 형식의 토큰 생성 (에러 케이스 테스트용)
     */
    fun createInvalidToken(): String {
        return "invalid.jwt.token"
    }
}