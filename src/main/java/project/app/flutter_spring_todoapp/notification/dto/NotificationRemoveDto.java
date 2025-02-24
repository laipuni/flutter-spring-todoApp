package project.app.flutter_spring_todoapp.notification.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationRemoveDto {

    private Long todoId;
    private Long deleterId;

    public static NotificationRemoveDto of(final Long todoId,final Long deleterId){
        return NotificationRemoveDto.builder()
                .todoId(todoId)
                .deleterId(deleterId)
                .build();
    }
}
