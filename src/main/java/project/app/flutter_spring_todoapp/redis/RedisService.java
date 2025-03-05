package project.app.flutter_spring_todoapp.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    public static final String NOTIFICATION_REMINDER = "reminder:";

    private final RedisTemplate<String,Object> redisTemplate;

    public void saveReminder(final ReminderMessage reminderMessage,final Long ttlSeconds){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if (isValidSaveReminder(reminderMessage, ttlSeconds)) {
            //Redis에 알림 데이터 저장 + TTL 설정
            operations.set(createReminderKey(reminderMessage.getNotificationId()),
                    reminderMessage, ttlSeconds, TimeUnit.SECONDS);
            log.info("{} {} redis 저장 ttl = {}",NOTIFICATION_REMINDER,reminderMessage.getNotificationId(),ttlSeconds);
        }
    }

    private static boolean isValidSaveReminder(final ReminderMessage reminderMessage, final Long ttlSeconds) {
        //redis에 알림 데이터를 저장할 수 있는지 검증
        return reminderMessage != null && reminderMessage.getNotificationId() != null && ttlSeconds > 0;
    }

    public void deleteReminder(final Long notificationId){
        if(notificationId != null){
            Set<String> keys = redisTemplate.keys(createReminderKey(notificationId));
            if (keys == null) return;
            redisTemplate.delete(keys);
            log.info("{} {} redis 삭제",NOTIFICATION_REMINDER,notificationId);
        }
    }

    public void updateReminder(final ReminderMessage reminderMessage){
        //redis에 ttl key 제거 후, 생성
        deleteReminder(reminderMessage.getNotificationId());
        saveReminder(reminderMessage,reminderMessage.getNotificationSecond());
    }

    private String createReminderKey(Long notificationId){
        return NOTIFICATION_REMINDER + notificationId;
    }
}
