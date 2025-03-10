package project.app.flutter_spring_todoapp.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.app.flutter_spring_todoapp.IntegrationTestSupport;
import project.app.flutter_spring_todoapp.notification.TimeType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static project.app.flutter_spring_todoapp.redis.RedisService.NOTIFICATION_REMINDER;


class RedisServiceTest extends IntegrationTestSupport {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @DisplayName("알림 설정할 데이터와 ttl을 받아 redis에 저장한다.")
    @Test
    void saveReminder() throws InterruptedException {
        //given
        Long notificationId = 1L;
        LocalDateTime dueTime = LocalDateTime.of(2025,3,4,1,35);
        TimeType fifteen = TimeType.FIFTEEN;
        ReminderMessage reminderMessage = ReminderMessage.builder()
                .notificationId(notificationId)
                .dueTime(dueTime)
                .timeType(fifteen)
                .build();
        Long ttlSeconds = 1L;
        //when
        redisService.saveReminder(reminderMessage,ttlSeconds);
        Thread.sleep(ttlSeconds);
        //then
        assertThat(redisTemplate.hasKey(NOTIFICATION_REMINDER+notificationId)).isTrue();

    }

    @DisplayName("Redis에 저장한 Reminder를 삭제한다")
    @Test
    void deleteReminder(){
        //given
        Long notificationId = 1L;

        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(NOTIFICATION_REMINDER + notificationId, notificationId);
        //when
        redisService.deleteReminder(notificationId);
        //then
        assertThat(redisTemplate.hasKey(NOTIFICATION_REMINDER+notificationId)).isFalse();
    }

    @DisplayName("Redis에 저장된 알림을 제거한 뒤 ttl을 변경후 알림을 저장한다.")
    @Test
    void updateReminder(){
        //given
        Long notificationId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueTime = LocalDateTime.of(now.getYear() + 1, now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        TimeType fifteen = TimeType.FIFTEEN;
        UpdateReminderMessage reminderMessage = UpdateReminderMessage.builder()
                .notificationId(notificationId)
                .dueTime(dueTime)
                .timeType(fifteen)
                .build();
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(NOTIFICATION_REMINDER + notificationId, notificationId);
        //when
        redisService.updateReminder(reminderMessage);
        redisTemplate.getExpire(NOTIFICATION_REMINDER+notificationId);
        //then
        assertThat(redisTemplate.hasKey(NOTIFICATION_REMINDER+notificationId)).isTrue();
        assertThat(redisTemplate.getExpire(NOTIFICATION_REMINDER+notificationId)).isEqualTo(reminderMessage.getNotificationSecond());
    }

}