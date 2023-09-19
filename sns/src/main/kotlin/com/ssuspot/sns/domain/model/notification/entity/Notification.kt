package com.ssuspot.sns.domain.model.notification.entity

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "notifications")
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val link: String,
    val message: String,
    var checked: Boolean = false
): BaseTimeEntity() {
    fun check() {
        this.checked = true
    }
}