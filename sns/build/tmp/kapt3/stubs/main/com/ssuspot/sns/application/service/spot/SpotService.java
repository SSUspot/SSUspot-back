package com.ssuspot.sns.application.service.spot;

@org.springframework.stereotype.Service
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0010H\u0017R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/ssuspot/sns/application/service/spot/SpotService;", "", "spotRepository", "Lcom/ssuspot/sns/infrastructure/repository/spot/SpotRepository;", "(Lcom/ssuspot/sns/infrastructure/repository/spot/SpotRepository;)V", "getSpotRepository", "()Lcom/ssuspot/sns/infrastructure/repository/spot/SpotRepository;", "createSpot", "Lcom/ssuspot/sns/application/dto/spot/CreateSpotResponseDto;", "createSpotDto", "Lcom/ssuspot/sns/application/dto/spot/CreateSpotDto;", "findValidSpot", "Lcom/ssuspot/sns/domain/model/spot/entity/Spot;", "spotId", "", "getAllSpot", "", "sns"})
public class SpotService {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.repository.spot.SpotRepository spotRepository = null;
    
    public SpotService(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.repository.spot.SpotRepository spotRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.infrastructure.repository.spot.SpotRepository getSpotRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.spot.CreateSpotResponseDto createSpot(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.spot.CreateSpotDto createSpotDto) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.ssuspot.sns.domain.model.spot.entity.Spot> getAllSpot() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.domain.model.spot.entity.Spot findValidSpot(long spotId) {
        return null;
    }
}