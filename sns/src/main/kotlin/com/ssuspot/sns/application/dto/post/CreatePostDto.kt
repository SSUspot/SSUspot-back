package com.ssuspot.sns.application.dto.post

class CreatePostDto(
        val title:String,
        val content:String,
        val imageUrls:String,
        val spotId:Long,
)