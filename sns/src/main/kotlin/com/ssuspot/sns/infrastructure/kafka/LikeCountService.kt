package com.ssuspot.sns.infrastructure.kafka

interface LikeCountService {
    fun getLikeCount(userId: String, hashtag: String): Long
}