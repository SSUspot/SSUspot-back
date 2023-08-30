package com.ssuspot.sns.core.spot.model.repository

import com.ssuspot.sns.core.spot.model.entity.Spot
import org.springframework.data.jpa.repository.JpaRepository

interface SpotRepository:JpaRepository<Spot, Long> {
}