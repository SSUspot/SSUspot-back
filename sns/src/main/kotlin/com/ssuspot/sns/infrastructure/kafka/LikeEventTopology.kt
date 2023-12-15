package com.ssuspot.sns.infrastructure.kafka

import com.ssuspot.sns.domain.model.post.event.LikeDataStoreEvent
import com.ssuspot.sns.domain.model.post.event.RatedLikeEvent
import com.ssuspot.sns.infrastructure.configs.KafkaStreamsConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.Stores
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.stereotype.Component

@Component
class LikeEventTopology(
    private val likeEventProcessor: LikeEventProcessor,
    private val likeCountService: LikeCountService
) {


    private lateinit var kafkaStreamsConfig: KafkaStreamsConfig
    fun buildTopology(builder: StreamsBuilder) {
        val storeName = "user-hashtag-likes-store"
        val storeSupplier = Stores.persistentKeyValueStore(storeName)
        val storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, Serdes.String(), Serdes.Long())

        builder.addStateStore(storeBuilder)

        val likeEventsStream = builder.stream<String, LikeDataStoreEvent>(
            "likes-topic",
            Consumed.with(Serdes.String(), JsonSerde<LikeDataStoreEvent>())
        )

        val processedLikesStream = likeEventsStream.mapValues { likeEvent ->
            val likeCount = likeCountService.getLikeCount(likeEvent.userId, likeEvent.tags.get(0))
            val additionalScore = likeEventProcessor.calculateAdditionalScore(likeEvent.userId, likeEvent.postId, likeCount)
            RatedLikeEvent(likeEvent.userId, likeEvent.postId, likeEvent.rating + additionalScore, likeEvent.timestamp)
        }

        processedLikesStream.to("processed-likes-topic", Produced.with(Serdes.String(), JsonSerde<RatedLikeEvent>()))
    }
}
