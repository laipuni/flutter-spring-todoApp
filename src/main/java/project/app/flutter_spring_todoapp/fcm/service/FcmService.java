package project.app.flutter_spring_todoapp.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.app.flutter_spring_todoapp.exception.fcm.FailedSendNotificationException;
import project.app.flutter_spring_todoapp.fcm.NotificationMessage;

@Slf4j
@Service
public class FcmService {

    // FCM 알림 전송 메서드
    public void sendNotification(final NotificationMessage notificationMessage) {
        Message message = Message.builder()
                .setToken(notificationMessage.getToken())
                .setNotification(
                        Notification.builder()
                        .setTitle(notificationMessage.getTitle())
                        .setBody(notificationMessage.getBody())
                        .build()
                )
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.debug("FCM 푸시 알림 전송 완료 result = {}",response);
        } catch (Exception e) {
            throw new FailedSendNotificationException(e.getMessage(), e.getCause());
        }
    }
}
