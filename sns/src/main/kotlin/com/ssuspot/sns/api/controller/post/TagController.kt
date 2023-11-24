package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.application.service.post.TagService
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val tagService: TagService,
) {

}