package com.ssuspot.sns.infrastructure.repository.spot

import com.ssuspot.sns.domain.model.spot.entity.Spot
import org.springframework.data.jpa.repository.JpaRepository

interface SpotRepository:JpaRepository<Spot, Long> {
}