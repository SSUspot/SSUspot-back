package com.ssuspot.sns.domain.model.alarm

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "alarms")
class CommentAlarm(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentAlarmId: Long? = 0L,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "post_user_id")
    var postUser: User,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "post_id")
    var post: Post,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "comment_user_id")
    var commentUser: User,

    @field:OneToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "comment_id")
    var comment: Comment,
): BaseTimeEntity()
