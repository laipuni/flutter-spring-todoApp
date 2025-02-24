package project.app.flutter_spring_todoapp.todo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TodoUpdateDto {

    private Long todoId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private TodoPriority priority;
    private TodoStatus status;
    private Long updaterId;

    public static TodoUpdateDto of(final Long todoId, final String title, final String description,
                                   final LocalDateTime startDate, final LocalDateTime dueDate, final TodoPriority priority,
                                   final TodoStatus status,final Long updaterId){
        return TodoUpdateDto.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(priority)
                .status(status)
                .updaterId(updaterId)
                .build();
    }
}
