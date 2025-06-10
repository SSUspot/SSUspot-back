package com.ssuspot.sns.infrastructure.configs

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Configuration
class MetricsConfig {

    @Bean
    fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry ->
            registry.config().commonTags(
                "application", "ssuspot-sns",
                "version", "1.0.0"
            )
        }
    }

    @Bean
    fun customMetricsFilter(meterRegistry: MeterRegistry): CustomMetricsFilter {
        return CustomMetricsFilter(meterRegistry)
    }
}

class CustomMetricsFilter(
    private val meterRegistry: MeterRegistry
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val startTime = System.nanoTime()
        
        try {
            filterChain.doFilter(request, response)
        } finally {
            val duration = System.nanoTime() - startTime
            val endpoint = request.requestURI
            val method = request.method
            val status = response.status.toString()
            
            // HTTP 요청 메트릭 기록
            Timer.builder("http.server.requests.custom")
                .tag("method", method)
                .tag("uri", endpoint)
                .tag("status", status)
                .tag("outcome", getOutcome(response.status))
                .register(meterRegistry)
                .record(duration, java.util.concurrent.TimeUnit.NANOSECONDS)
                
            // 에러율 메트릭
            meterRegistry.counter("http.requests.total", 
                "method", method,
                "endpoint", endpoint,
                "status", status
            ).increment()
            
            // 응답 시간별 분류
            val responseTimeCategory = when {
                duration < 100_000_000 -> "fast" // < 100ms
                duration < 500_000_000 -> "medium" // < 500ms  
                duration < 1_000_000_000 -> "slow" // < 1s
                else -> "very_slow" // >= 1s
            }
            
            meterRegistry.counter("http.response.time.category",
                "category", responseTimeCategory,
                "endpoint", endpoint
            ).increment()
        }
    }
    
    private fun getOutcome(status: Int): String {
        return when (status) {
            in 200..299 -> "SUCCESS"
            in 300..399 -> "REDIRECTION"
            in 400..499 -> "CLIENT_ERROR"
            in 500..599 -> "SERVER_ERROR"
            else -> "UNKNOWN"
        }
    }
}