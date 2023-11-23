package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.dto.post.GetTagRequestDto
import com.ssuspot.sns.application.service.post.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val tagService: TagService
) {
    //특정 태그의 게시물들 가져오기
    @GetMapping("/api/tags")
    fun getPostsByTagName(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("tagName") tagName: String
    ): ResponseEntity<List<PostResponse>> {
        val posts = tagService.findPostsByTagName(GetTagRequestDto(page, size, tagName))
        return ResponseEntity.ok(
            posts.map {
                PostResponse(
                    it.id,
                    it.title,
                    it.content,
                    it.nickname,
                    it.imageUrls,
                    it.tags,
                    it.spotId
                )
            }
        )
    }
}