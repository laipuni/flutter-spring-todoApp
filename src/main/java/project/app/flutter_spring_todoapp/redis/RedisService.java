package project.app.flutter_spring_todoapp.redis;

import jakarta.annotation.Nullable;
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

    public void saveReminder(final ReminderMessage reminderMessage, final Long ttlSeconds){
        //Redis에 알림 데이터 저장 + TTL 설정
        setDataWithTtl(createReminderKey(reminderMessage.getNotificationId()),
                reminderMessage.getNotificationId(), ttlSeconds, TimeUnit.SECONDS);
    }

    private void setDataWithTtl(final String key, final Object value, final long ttl, final TimeUnit timeUnit){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if(value != null && ttl > 0 && timeUnit != null){
            operations.set(key,value,ttl,timeUnit);
            log.info("{} redis 저장 ttl = {}",key, ttl);
        }
    }

    public void deleteReminder(final Long notificationId){
        if(notificationId != null){
            Set<String> keys = redisTemplate.keys(createReminderKey(notificationId));
            if (keys == null) return;
            redisTemplate.delete(keys);
            log.info("{} {} redis 삭제",NOTIFICATION_REMINDER,notificationId);
        }
    }

    public void updateReminder(final UpdateReminderMessage reminderMessage){
        //redis에 ttl key 제거 후, 생성
        deleteReminder(reminderMessage.getNotificationId());
        Long ttl = reminderMessage.getNotificationSecond();
        setDataWithTtl(createReminderKey(reminderMessage.getNotificationId()),
                reminderMessage.getNotificationId(), ttl, TimeUnit.SECONDS);
    }

    private String createReminderKey(Long notificationId){
        return NOTIFICATION_REMINDER + notificationId;
    }
}
