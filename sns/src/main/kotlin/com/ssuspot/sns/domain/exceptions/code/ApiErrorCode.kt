package com.ssuspot.sns.domain.exceptions.code

enum class ApiErrorCode(
    val code: String,
) {
    COMMON_ERROR("E0001"),
    INVALID_FORMAT("E0002"),
    MISSING_PARAMETER("E0003"),
    JSON_CONVERT_ERROR("E0004"),
    NOT_FOUND("E0005"),
    NOT_FOUND_ENTITY("E0006"),
    VALIDATION_ERROR("E0007"),
}