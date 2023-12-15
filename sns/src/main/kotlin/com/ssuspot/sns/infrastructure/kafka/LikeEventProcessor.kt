package com.ssuspot.sns.infrastructure.kafka

import com.ssuspot.sns.infrastructure.configs.KafkaStreamsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LikeEventProcessor {
    @Autowired
    private lateinit var kafkaStreamsConfig: KafkaStreamsConfig
    fun calculateAdditionalScore(userId: String, postId: String, likeCount: Long): Double {
        // likeCount 기반으로 추가 점수 계산
        val threshold = 10
        return if (likeCount > threshold) {
            // likeCount가 임계값을 초과하는 경우 추가 점수 부여
            1.0
        } else {
            // 그렇지 않은 경우 추가 점수 없음
            0.0
        }
    }
}
