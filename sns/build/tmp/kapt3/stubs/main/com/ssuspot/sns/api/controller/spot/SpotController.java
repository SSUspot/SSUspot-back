package com.ssuspot.sns.api.controller.spot;

@org.springframework.web.bind.annotation.RestController
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\nH\u0017J\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\fH\u0017R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/ssuspot/sns/api/controller/spot/SpotController;", "", "spotService", "Lcom/ssuspot/sns/application/service/spot/SpotService;", "(Lcom/ssuspot/sns/application/service/spot/SpotService;)V", "getSpotService", "()Lcom/ssuspot/sns/application/service/spot/SpotService;", "createSpot", "", "createSpotRequest", "Lcom/ssuspot/sns/api/request/spot/CreateSpotRequest;", "getSpotList", "Lorg/springframework/http/ResponseEntity;", "", "Lcom/ssuspot/sns/api/response/spot/SpotResponse;", "sns"})
public class SpotController {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.spot.SpotService spotService = null;
    
    public SpotController(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.spot.SpotService spotService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.service.spot.SpotService getSpotService() {
        return null;
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/api/spots"})
    public void createSpot(@org.springframework.web.bind.annotation.RequestBody
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.api.request.spot.CreateSpotRequest createSpotRequest) {
    }
    
    @org.springframework.web.bind.annotation.GetMapping(value = {"/api/spots"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<java.util.List<com.ssuspot.sns.api.response.spot.SpotResponse>> getSpotList() {
        return null;
    }
}