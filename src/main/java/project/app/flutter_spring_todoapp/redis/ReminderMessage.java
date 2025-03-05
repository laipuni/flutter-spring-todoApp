package project.app.flutter_spring_todoapp.redis;

import com.google.auto.value.extension.serializable.SerializableAutoValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReminderMessage implements Serializable {

    private String fcmToken;
    private String title;
    private String content;
    private Long notificationId;
    private LocalDateTime dueTime;
    private TimeType timeType;

    public static ReminderMessage of(final String fcmToken, final String title, final String content,
                                     final Long notificationId, final LocalDateTime dueTime, final TimeType timeType){
        return ReminderMessage.builder()
                .fcmToken(fcmToken)
                .title(title)
                .content(content)
                .notificationId(notificationId)
                .dueTime(dueTime)
                .timeType(timeType)
                .build();
    }

    public static ReminderMessage of(final Notification notification){
        return of(notification.getMember().getFcmToken(),notification.getTitle(),notification.getContent(),
                notification.getId(),notification.getDueTime(),notification.getTimeType());
    }

    public Long getNotificationSecond(){
        //알림을 보낼 정확한 시간 계산
        LocalDateTime reminderTime = this.dueTime.minusMinutes(this.timeType.getTime());
        //현재 시간과의 차이를 초 단위로 계산 (TTL)
        return Duration.between(LocalDateTime.now(), reminderTime).getSeconds();
    }

}

