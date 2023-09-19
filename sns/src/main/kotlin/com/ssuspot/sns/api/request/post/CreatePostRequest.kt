package com.ssuspot.sns.api.request.post

class CreatePostRequest(
        val title:String,
        val content:String,
        val imageUrls:List<String>,
        val spotId:Long,
)