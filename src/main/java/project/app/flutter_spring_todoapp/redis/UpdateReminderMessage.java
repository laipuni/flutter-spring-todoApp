package project.app.flutter_spring_todoapp.redis;

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
public class UpdateReminderMessage implements Serializable {

    private Long notificationId;
    private LocalDateTime dueTime;
    private TimeType timeType;

    public static UpdateReminderMessage of(final Long notificationId,
                                           final LocalDateTime dueTime, final TimeType timeType){
        return UpdateReminderMessage.builder()
                .notificationId(notificationId)
                .dueTime(dueTime)
                .timeType(timeType)
                .build();
    }


    public Long getNotificationSecond(){
        //알림을 보낼 정확한 시간 계산
        LocalDateTime reminderTime = this.dueTime.minusMinutes(this.timeType.getTime());
        //현재 시간과의 차이를 초 단위로 계산 (TTL)
        return Duration.between(LocalDateTime.now(), reminderTime).getSeconds();
    }
}
