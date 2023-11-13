package com.ssuspot.sns.domain.exceptions.code

enum class ErrorCode(
    val message: String,
) {
    UNKNOWN("알수없는 오류"),

    NOT_EXISTS_USER("존재하지 않는 유저입니다"),
    ALREADY_EXISTS_USER("이미 존재하는 유저입니다"),
    INVALID_USER_PASSWORD("이메일 혹은 비밀번호가 틀렸습니다"),
    INVALID_TOKEN("유효하지 않은 토큰입니다"),

    NOT_EXISTS_POST("존재하지 않는 게시글입니다"),
    INVALID_TEXT("유효하지 않은 텍스트입니다"),
    POST_PERMISSION_DENIED("게시물 접근 권한이 없습니다"),
    POST_CONTENT_TOO_SHORT("게시물 내용이 너무 짧습니다"),

    NOT_EXISTS_COMMENT("존재하지 않는 댓글입니다"),
    COMMENT_PERMISSION_DENIED("댓글 접근 권한이 없습니다"),
    COMMENT_CONTENT_TOO_LONG("댓글 내용이 너무 깁니다"),

    SPOT_NOT_EXISTS("존재하지 않는 스팟입니다"),

    RATE_LIMIT_EXCEEDED("요청 횟수가 너무 많습니다"),
}