package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.domain.exceptions.post.TagNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import com.ssuspot.sns.infrastructure.repository.post.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val customPostRepository: CustomPostRepository
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
}
