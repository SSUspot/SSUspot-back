package com.ssuspot.sns.infrastructure.configs
/*
import com.ssuspot.sns.domain.model.post.event.LikeDataStoreEvent
import com.ssuspot.sns.infrastructure.kafka.LikeEventTopology
import org.apache.kafka.streams.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.util.Properties

@Configuration
class KafkaStreamsConfig {

    @Autowired
    private lateinit var likeEventTopology: LikeEventTopology

    private lateinit var kafkaStreams: KafkaStreams

    @Bean
    fun kafkaStreams(): KafkaStreams {
        val props = Properties()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "your-application-id"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        // 여기에 필요한 추가 Kafka Streams 설정을 추가합니다.

        val builder = StreamsBuilder()
        likeEventTopology.buildTopology(builder) // 토폴로지 구성

        kafkaStreams = KafkaStreams(builder.build(), props)
        kafkaStreams.start()

        return kafkaStreams
    }

    @Bean
    fun userHashtagLikesStore(): ReadOnlyKeyValueStore<String, List<LikeDataStoreEvent>> {
        val storeQueryParameters = StoreQueryParameters.fromNameAndType(
            "user-hashtag-likes-store",
            QueryableStoreTypes.keyValueStore<String, List<LikeDataStoreEvent>>()
        )
        return kafkaStreams.store(storeQueryParameters)
    }

    fun getLikeCount(userId: String, hashtag: String): Long {
        val store = userHashtagLikesStore()
        val likeEvents = store.get("$userId-$hashtag") ?: emptyList()
        val currentTime = Instant.now().toEpochMilli()
        val twentyFourHoursAgo = currentTime - 24 * 60 * 60 * 1000
        return likeEvents.count { it.timestamp >= twentyFourHoursAgo }.toLong()
    }
}

 */
