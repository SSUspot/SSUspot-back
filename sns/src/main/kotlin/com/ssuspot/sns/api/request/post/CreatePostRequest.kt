package com.ssuspot.sns.api.request.post

import jakarta.validation.constraints.*

data class CreatePostRequest(
    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
    @field:Pattern(
        regexp = "^[^<>\"'&\n\r\t\u0000]*$",
        message = "제목에 허용되지 않는 문자가 포함되어 있습니다"
    )
    val title: String,
    
    @field:NotBlank(message = "내용은 필수입니다")
    @field:Size(min = 1, max = 5000, message = "내용은 1자 이상 5000자 이하여야 합니다")
    @field:Pattern(
        regexp = "^[^<>\"'&\u0000]*$",
        message = "내용에 허용되지 않는 문자가 포함되어 있습니다"
    )
    val content: String,
    
    @field:Size(max = 10, message = "이미지는 최대 10개까지 업로드 가능합니다")
    val imageUrls: List<String> = emptyList(),
    
    @field:Size(max = 10, message = "태그는 최대 10개까지 추가 가능합니다")
    val tags: List<String> = emptyList(),
    
    @field:Positive(message = "올바른 스팟 ID를 입력해주세요")
    val spotId: Long,
)