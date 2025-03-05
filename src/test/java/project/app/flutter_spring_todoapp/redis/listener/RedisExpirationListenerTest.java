package project.app.flutter_spring_todoapp.redis.listener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.app.flutter_spring_todoapp.fcm.NotificationMessage;
import project.app.flutter_spring_todoapp.fcm.service.FcmService;
import project.app.flutter_spring_todoapp.redis.RedisService;
import project.app.flutter_spring_todoapp.redis.ReminderMessage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisExpirationListenerTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @MockBean
    FcmService fcmService;

    @DisplayName("ttl을 지정한 key가 만료될 때 RedisExpirationListener의 onMessage가 발동된다.")
    @Test
    void onMessage() throws InterruptedException {
        //given
        Long notificationId = 1L;
        long ttlSeconds = 1L;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(RedisService.NOTIFICATION_REMINDER + notificationId,notificationId,ttlSeconds, TimeUnit.MILLISECONDS);

        //when
        //then
        Thread.sleep(ttlSeconds);
    }


}