package com.ssuspot.sns.utils.time

import java.time.ZoneId
import java.time.ZonedDateTime

object EpochTimeUtil {

    private const val TIME_ZONE = "Asia/Seoul"

    fun getCurrentEpochTime() = ZonedDateTime.now(ZoneId.of(TIME_ZONE))
        .toInstant()
        .toEpochMilli()
}