package com.ssuspot.sns.domain.model.notification.entity

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "notifications")
class Notification(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:Column(name = "title")
    val title: String,
    @field:Column(name = "link")
    val link: String,
    @field:Column(name = "message")
    val message: String,
    @field:Column(name = "checked")
    var checked: Boolean = false
) : BaseTimeEntity() {
    fun read() {
        this.checked = true
    }
}