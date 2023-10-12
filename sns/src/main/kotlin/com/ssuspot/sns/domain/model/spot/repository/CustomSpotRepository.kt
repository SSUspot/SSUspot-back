package com.ssuspot.sns.domain.model.spot.repository

import com.ssuspot.sns.domain.model.spot.entity.Spot

interface CustomSpotRepository {
    fun findSpotById(spotId: Long): Spot?
}