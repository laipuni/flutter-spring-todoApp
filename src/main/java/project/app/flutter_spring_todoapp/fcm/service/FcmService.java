package project.app.flutter_spring_todoapp.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.app.flutter_spring_todoapp.exception.fcm.FailedSendFcmException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class FcmService {

    // FCM 알림 전송 메서드
    public void sendNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .build();

        try {
            Thread.sleep(1000);
            String response = FirebaseMessaging.getInstance().send(message);
            log.debug("FCM 푸시 알림 전송 완료 result = {}",response);
        } catch (Exception e) {
            throw new FailedSendFcmException("알림 전송에 실패했습니다.",e.getCause());
        }
    }
}
