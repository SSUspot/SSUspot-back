package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.GetTagRequestDto
import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.exceptions.post.TagNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.infrastructure.repository.post.TagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    fun createTag(name: String): Tag {
        return tagRepository.save(Tag(tagName = name))
    }

    fun findValidTagByName(name: String): Tag? {
        return tagRepository.findTagByTagName(name)
    }

    fun findValidPostsByTagName(name: String): List<Post> {
        val tag = tagRepository.findTagByTagName(name) ?: throw TagNotFoundException()
        return tag.postTags.map { it.post }
    }

    fun findValidTagById(id: Long): Tag {
        return tagRepository.findById(id).orElseThrow { TagNotFoundException() }
    }

    fun findPostsByTagName(request: GetTagRequestDto): List<PostResponseDto> {
        val posts = tagRepository.findPostsByTagName(request.tagName, toPageableLatestSort(request.page, request.size)) ?: throw PostNotFoundException()
        return posts.content.map {
            PostResponseDto(
                id = it.id!!,
                title = it.title,
                content = it.content,
                nickname = it.user.nickname,
                imageUrls = it.imageUrls,
                tags = it.postTags.map { it.tag.tagName },
                spotId = it.spot.id!!
            )
        }
    }

    private fun toPageableLatestSort(page: Int, size: Int) = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id")
}
