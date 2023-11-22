package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.PostTagDto
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.exceptions.post.PostTagNotFoundException
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.infrastructure.repository.post.PostTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostTagService(
    private val postTagRepository: PostTagRepository,
    private val postService: PostService,
    private val tagService: TagService
) {
    @Transactional
    fun createPostTag(postTagDto: PostTagDto): PostTagDto {
        val postTag = PostTag(
            post = postService.findValidPostById(postTagDto.postId),
            tag = tagService.findValidTagById(postTagDto.tagId)
        )
        val savedPostTag = postTagRepository.save(postTag)
        return savedPostTag.toDto()
    }

    @Transactional(readOnly = true)
    fun getPostTagsByPostId(postId: Long): List<PostTagDto> {
        val postTags = postTagRepository.findPostTagsByPostId(postId) ?: throw PostNotFoundException()
        return postTags.map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun getPostTagsByTagName(tagName: String): List<PostTagDto> {
        val postTags = postTagRepository.findPostTagsByTagName(tagName) ?: throw PostTagNotFoundException()
        return postTags.map { it.toDto() }
    }

    private fun PostTag.toDto(): PostTagDto {
        return PostTagDto(
            postId = this.post.id!!,
            tagId = this.tag.id!!
        )
    }
}
