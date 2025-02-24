package project.app.flutter_spring_todoapp.notification.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.notification.TimeType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationUpdateDto {

    private Long todoId;
    private String title;
    private TimeType timeType;
    private Long updaterId;

    public static NotificationUpdateDto of(final Long todoId, final String title, final TimeType timeType,final Long updaterId){
        return NotificationUpdateDto.builder()
                .todoId(todoId)
                .title(title)
                .timeType(timeType)
                .updaterId(updaterId)
                .build();
    }

}
