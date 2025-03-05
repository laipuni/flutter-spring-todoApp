package project.app.flutter_spring_todoapp.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import project.app.flutter_spring_todoapp.exception.fcm.FailedSendNotificationException;
import project.app.flutter_spring_todoapp.fcm.NotificationMessage;
import project.app.flutter_spring_todoapp.fcm.service.FcmService;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.repository.NotificationRepository;

import static project.app.flutter_spring_todoapp.redis.RedisService.NOTIFICATION_REMINDER;

@Slf4j
@Component
public class RedisExpirationListener extends KeyExpirationEventMessageListener {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    public RedisExpirationListener(final RedisMessageListenerContainer listenerContainer,
                                   final NotificationRepository notificationRepository,
                                   final FcmService fcmService) {
        super(listenerContainer);
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
    }

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        String keyspace = message.toString();
        if(keyspace.startsWith(NOTIFICATION_REMINDER)){
            String[] split = keyspace.split(NOTIFICATION_REMINDER);
            Long notificationId = Long.parseLong(split[1]);
            Notification notification = notificationRepository.findNotificationWithMemberByTodoId(notificationId)
                    .orElseThrow(() -> new FailedSendNotificationException("알림(id : {})이 존재하지 않아 알림 전송을 실패했습니다."));
            fcmService.sendNotification(NotificationMessage.of(notification));
            log.info("keySpace expired event occurs, pattern {} | {}", new String(pattern), keyspace);
        }
    }
}
