package com.ssuspot.sns.infrastructure.repository.spot

import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.spot.repository.CustomSpotRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpotRepository:JpaRepository<Spot, Long>, CustomSpotRepository