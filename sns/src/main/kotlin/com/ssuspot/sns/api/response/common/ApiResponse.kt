package com.ssuspot.sns.api.response.common

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 표준 API 응답 래퍼
 * 모든 API 응답에 일관된 형태를 제공
 */
@Schema(description = "표준 API 응답 형태")
data class ApiResponse<T>(
    @Schema(description = "응답 데이터", example = "실제 응답 데이터")
    val data: T? = null,
    
    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다")
    val message: String = "Success",
    
    @Schema(description = "성공 여부", example = "true")
    val success: Boolean = true,
    
    @Schema(description = "응답 시간", example = "2024-01-01T12:00:00")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @Schema(description = "HTTP 상태 코드", example = "200")
    val status: Int = 200
) {
    companion object {
        /**
         * 성공 응답 생성
         */
        fun <T> success(data: T? = null, message: String = "요청이 성공적으로 처리되었습니다"): ApiResponse<T> {
            return ApiResponse(
                data = data,
                message = message,
                success = true,
                status = 200
            )
        }
        
        /**
         * 생성 성공 응답 (201)
         */
        fun <T> created(data: T, message: String = "리소스가 성공적으로 생성되었습니다"): ApiResponse<T> {
            return ApiResponse(
                data = data,
                message = message,
                success = true,
                status = 201
            )
        }
        
        /**
         * 에러 응답 생성
         */
        fun <T> error(message: String, status: Int = 400): ApiResponse<T> {
            return ApiResponse(
                data = null,
                message = message,
                success = false,
                status = status
            )
        }
    }
}