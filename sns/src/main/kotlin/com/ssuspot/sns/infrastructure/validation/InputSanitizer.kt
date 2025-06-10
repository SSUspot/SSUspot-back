package com.ssuspot.sns.infrastructure.validation

import org.springframework.stereotype.Component
import java.util.regex.Pattern

/**
 * 입력 데이터 검증 및 무결성 확인을 위한 유틸리티 클래스
 * XSS, SQL Injection 등의 공격을 방지합니다.
 */
@Component
class InputSanitizer {

    companion object {
        // XSS 공격 패턴들
        private val XSS_PATTERNS = listOf(
            Pattern.compile(".*<script.*?>.*</script>.*", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile(".*javascript:.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*on\\w+\\s*=.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<iframe.*?>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<object.*?>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<embed.*?>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<link.*?>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<meta.*?>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*eval\\s*\\(.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*expression\\s*\\(.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*vbscript:.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*data:.*", Pattern.CASE_INSENSITIVE)
        )

        // SQL Injection 패턴들
        private val SQL_INJECTION_PATTERNS = listOf(
            Pattern.compile(".*('|(\\-\\-)|(;)|(\\|)|(\\*)|(%))", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*(union|select|insert|delete|update|drop|create|alter|exec|execute)", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\b(and|or)\\b.*\\b(\\d+\\s*=\\s*\\d+|true|false)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\b(concat|group_concat|substring|ascii|char|length)\\s*\\(.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\b(information_schema|sys|mysql|pg_|msdb)\\b.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\bcast\\s*\\(.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*0x[0-9a-f]+.*", Pattern.CASE_INSENSITIVE)
        )

        // 파일 경로 조작 패턴들
        private val PATH_TRAVERSAL_PATTERNS = listOf(
            Pattern.compile(".*\\.\\./.*"),
            Pattern.compile(".*\\.\\\\.*"),
            Pattern.compile(".*%2e%2e%2f.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*%2e%2e%5c.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*%252e%252e%252f.*", Pattern.CASE_INSENSITIVE)
        )

        // 허용되지 않는 특수 문자들
        private val DANGEROUS_CHARS = charArrayOf(
            '<', '>', '"', '\'', '&', '\n', '\r', '\t', '\u0000'
        )

        // 이메일 검증 패턴
        private val EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )

        // 사용자명 검증 패턴 (영문, 숫자, 일부 특수문자만 허용)
        private val USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._-]{3,20}$"
        )

        // 닉네임 검증 패턴 (한글, 영문, 숫자 허용)
        private val NICKNAME_PATTERN = Pattern.compile(
            "^[가-힣a-zA-Z0-9 ]{2,10}$"
        )
    }

    /**
     * XSS 공격 패턴을 검사합니다.
     */
    fun containsXSS(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return XSS_PATTERNS.any { it.matcher(input).matches() }
    }

    /**
     * SQL Injection 공격 패턴을 검사합니다.
     */
    fun containsSQLInjection(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return SQL_INJECTION_PATTERNS.any { it.matcher(input).matches() }
    }

    /**
     * 경로 조작 공격 패턴을 검사합니다.
     */
    fun containsPathTraversal(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return PATH_TRAVERSAL_PATTERNS.any { it.matcher(input).matches() }
    }

    /**
     * 위험한 문자가 포함되어 있는지 검사합니다.
     */
    fun containsDangerousChars(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return input.any { it in DANGEROUS_CHARS }
    }

    /**
     * 입력값이 안전한지 종합적으로 검사합니다.
     */
    fun isSafeInput(input: String?): Boolean {
        return !containsXSS(input) &&
                !containsSQLInjection(input) &&
                !containsPathTraversal(input) &&
                !containsDangerousChars(input)
    }

    /**
     * 문자열을 안전하게 정제합니다.
     */
    fun sanitizeString(input: String?): String {
        if (input.isNullOrBlank()) return ""
        
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("&", "&amp;")
            .replace("\n", "")
            .replace("\r", "")
            .replace("\t", " ")
            .replace("\u0000", "")
            .trim()
    }

    /**
     * HTML 태그를 완전히 제거합니다.
     */
    fun stripHtmlTags(input: String?): String {
        if (input.isNullOrBlank()) return ""
        return input.replace(Regex("<[^>]*>"), "").trim()
    }

    /**
     * 이메일 형식을 검증합니다.
     */
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return EMAIL_PATTERN.matcher(email).matches() && isSafeInput(email)
    }

    /**
     * 사용자명 형식을 검증합니다.
     */
    fun isValidUsername(username: String?): Boolean {
        if (username.isNullOrBlank()) return false
        return USERNAME_PATTERN.matcher(username).matches() && isSafeInput(username)
    }

    /**
     * 닉네임 형식을 검증합니다.
     */
    fun isValidNickname(nickname: String?): Boolean {
        if (nickname.isNullOrBlank()) return false
        return NICKNAME_PATTERN.matcher(nickname).matches() && isSafeInput(nickname)
    }

    /**
     * 게시글 제목을 검증하고 정제합니다.
     */
    fun sanitizePostTitle(title: String?): String {
        if (title.isNullOrBlank()) return ""
        
        val sanitized = stripHtmlTags(title).take(100) // 최대 100자 제한
        
        if (!isSafeInput(sanitized)) {
            throw IllegalArgumentException("제목에 허용되지 않는 문자나 패턴이 포함되어 있습니다.")
        }
        
        return sanitized.trim()
    }

    /**
     * 게시글 내용을 검증하고 정제합니다.
     */
    fun sanitizePostContent(content: String?): String {
        if (content.isNullOrBlank()) return ""
        
        val sanitized = stripHtmlTags(content).take(5000) // 최대 5000자 제한
        
        if (!isSafeInput(sanitized)) {
            throw IllegalArgumentException("내용에 허용되지 않는 문자나 패턴이 포함되어 있습니다.")
        }
        
        return sanitized.trim()
    }

    /**
     * 태그명을 검증하고 정제합니다.
     */
    fun sanitizeTagName(tagName: String?): String {
        if (tagName.isNullOrBlank()) return ""
        
        val sanitized = stripHtmlTags(tagName)
            .replace(Regex("[^가-힣a-zA-Z0-9_-]"), "") // 한글, 영문, 숫자, _, - 만 허용
            .take(20) // 최대 20자 제한
        
        if (sanitized.length < 2) {
            throw IllegalArgumentException("태그는 최소 2자 이상이어야 합니다.")
        }
        
        return sanitized.trim()
    }

    /**
     * URL을 검증합니다.
     */
    fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        
        // 기본 URL 패턴 검증
        val urlPattern = Pattern.compile(
            "^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$"
        )
        
        return urlPattern.matcher(url).matches() && 
               isSafeInput(url) && 
               !containsPathTraversal(url)
    }

    /**
     * 파일명을 검증하고 정제합니다.
     */
    fun sanitizeFileName(fileName: String?): String {
        if (fileName.isNullOrBlank()) return ""
        
        val sanitized = fileName
            .replace(Regex("[^a-zA-Z0-9._-]"), "_") // 안전한 문자만 허용
            .take(100) // 최대 100자 제한
        
        if (containsPathTraversal(sanitized)) {
            throw IllegalArgumentException("파일명에 허용되지 않는 패턴이 포함되어 있습니다.")
        }
        
        return sanitized
    }
}