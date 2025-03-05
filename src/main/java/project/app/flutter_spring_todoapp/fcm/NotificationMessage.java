package project.app.flutter_spring_todoapp.fcm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.notification.Notification;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationMessage {

    private String token;
    private String title;
    private String body;

    public static NotificationMessage of(final Notification notification){
        return NotificationMessage.builder()
                .token(notification.getMember().getFcmToken())
                .title(notification.getTitle())
                .body(notification.getContent())
                .build();
    }

}
