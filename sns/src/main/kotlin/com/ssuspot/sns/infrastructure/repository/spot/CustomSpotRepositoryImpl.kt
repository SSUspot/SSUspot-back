package com.ssuspot.sns.infrastructure.repository.spot

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.spot.entity.QSpot
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.spot.repository.CustomSpotRepository
import org.springframework.stereotype.Repository

@Repository
class CustomSpotRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomSpotRepository {
    override fun findSpotById(spotId: Long): Spot? {
        return queryFactory
            .selectFrom(QSpot.spot)
            .where(QSpot.spot.id.eq(spotId))
            .fetchOne()
    }
}