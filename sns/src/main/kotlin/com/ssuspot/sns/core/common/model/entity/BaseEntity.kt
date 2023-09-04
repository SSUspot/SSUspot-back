package com.ssuspot.sns.core.common.model.entity

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import com.ssuspot.sns.utils.time.EpochTimeUtil.getCurrentEpochTime

@MappedSuperclass
open class BaseTimeEntity(
    @field:Column(name = "created_at")
    var createdAt: Long? = null,

    @field:Column(name = "updated_at")
    var updatedAt: Long? = null
) {

    @PrePersist
    fun onCreate() {
        createdAt = getCurrentEpochTime()
        updatedAt = createdAt
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = getCurrentEpochTime()
    }
}
