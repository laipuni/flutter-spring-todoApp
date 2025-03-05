package project.app.flutter_spring_todoapp.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.app.flutter_spring_todoapp.notification.TimeType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static project.app.flutter_spring_todoapp.redis.RedisService.NOTIFICATION_REMINDER;


@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @DisplayName("알림 설정할 데이터와 ttl을 받아 redis에 저장한다.")
    @Test
    void saveReminder() throws InterruptedException {
        //given
        String fcmToken = "fcmToken";
        Long notificationId = 1L;
        LocalDateTime dueTime = LocalDateTime.of(2025,3,4,1,35);
        TimeType fifteen = TimeType.FIFTEEN;
        ReminderMessage reminderMessage = ReminderMessage.builder()
                .fcmToken(fcmToken)
                .notificationId(notificationId)
                .title("\"할일\"이 " + fifteen.getDescription() + " 남았습니다")
                .content("자세히 확인하시려면 해당 알림을 클릭해주세요.")
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
        String fcmToken = "fcmToken";
        Long notificationId = 1L;
        LocalDateTime dueTime = LocalDateTime.of(2025,3,4,1,35);
        TimeType fifteen = TimeType.FIFTEEN;
        ReminderMessage reminderMessage = ReminderMessage.builder()
                .fcmToken(fcmToken)
                .notificationId(notificationId)
                .title("\"할일\"이 " + fifteen.getDescription() + " 남았습니다")
                .content("자세히 확인하시려면 해당 알림을 클릭해주세요.")
                .dueTime(dueTime)
                .timeType(fifteen)
                .build();
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(NOTIFICATION_REMINDER + notificationId, reminderMessage);
        //when
        redisService.deleteReminder(notificationId);
        //then
        assertThat(redisTemplate.hasKey(NOTIFICATION_REMINDER+notificationId)).isFalse();
    }

}