package project.app.flutter_spring_todoapp.notification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationSaveDto {

    private Long todoId;
    private String title;
    private LocalDateTime duetDate;
    private TimeType timeType;

    public static NotificationSaveDto of(final Long todoId, final String title, final LocalDateTime duetDate, final TimeType timeType){
        return NotificationSaveDto.builder()
                .todoId(todoId)
                .title(title)
                .duetDate(duetDate)
                .timeType(timeType)
                .build();
    }

}
