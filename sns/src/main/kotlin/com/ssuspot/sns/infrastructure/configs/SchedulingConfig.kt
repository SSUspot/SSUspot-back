package com.ssuspot.sns.infrastructure.configs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executors

/**
 * 스케줄링 설정
 * 비동기 스케줄링과 모니터링 작업을 위한 설정
 */
@Configuration
@EnableScheduling
class SchedulingConfig : SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        // 스케줄링 작업을 위한 전용 스레드 풀 설정
        taskRegistrar.setScheduler(
            Executors.newScheduledThreadPool(5) { runnable ->
                Thread(runnable, "monitoring-scheduler").apply {
                    isDaemon = true
                }
            }
        )
    }
}