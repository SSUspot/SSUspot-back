package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.alarm.AlarmResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.service.alarm.AlarmService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AlarmController(
    val alarmService: AlarmService,
) {
    @GetMapping("/api/alarms/")
    fun getAlarms(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<AlarmResponse>> {
        val alarms = alarmService.getAlarms(page, size, authInfo.email)
        return ResponseEntity.ok(
            alarms.map {
                AlarmResponse(
                    alarmId = it.alarmId,
                    articleTitle = it.articleTitle,
                    commentContent = it.commentContent,
                )
            }
        )
    }
}