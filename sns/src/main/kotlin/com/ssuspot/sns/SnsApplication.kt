package com.ssuspot.sns

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.kafka.annotation.EnableKafkaStreams

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
//@EnableKafkaStreams
class SnsApplication

fun main(args: Array<String>) {
	runApplication<SnsApplication>(*args)
}
