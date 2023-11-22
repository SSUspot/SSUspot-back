package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.exceptions.post.TagNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.infrastructure.repository.post.TagRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    fun findValidTagByName(name: String): Tag {
        return tagRepository.findTagByName(name) ?: throw TagNotFoundException()
    }
    fun findValidPostsByName(name: String): List<Post> {
        val tag = tagRepository.findTagByName(name) ?: throw TagNotFoundException()
        return tag.postTags.map { it.post }
    }
    fun findValidTagById(id: Long): Tag {
        return tagRepository.findById(id).orElseThrow { TagNotFoundException() }
    }
    fun findPostsByTagName(tagName: String, pageable:Pageable): List<Post> {
        val posts = tagRepository.findPostsByTagName(tagName, pageable) ?: throw PostNotFoundException()
        return posts.content
    }
}