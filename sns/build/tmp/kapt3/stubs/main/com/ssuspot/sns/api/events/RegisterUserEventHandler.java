package com.ssuspot.sns.api.events;

@org.springframework.stereotype.Component
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0017J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0017\u00a8\u0006\b"}, d2 = {"Lcom/ssuspot/sns/api/events/RegisterUserEventHandler;", "", "()V", "createTimeline", "", "event", "Lcom/ssuspot/sns/domain/model/user/event/RegisteredUserEvent;", "sendSuccessMail", "sns"})
public class RegisterUserEventHandler {
    
    public RegisterUserEventHandler() {
        super();
    }
    
    @org.springframework.context.event.EventListener
    @org.springframework.scheduling.annotation.Async
    public void createTimeline(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent event) {
    }
    
    @org.springframework.context.event.EventListener
    @org.springframework.scheduling.annotation.Async
    public void sendSuccessMail(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent event) {
    }
}