package project.app.flutter_spring_todoapp.todo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateTodoRequest {

    @NotNull(message = "수정할 할일의 id는 필수 값 입니다.")
    private Long todoId;

    @NotEmpty(message = "제목은 필수 입력칸 입니다.")
    @Size(max = 32,message = "최대 32자까지 작성할 수 있습니다.")
    private String title;

    private String description;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "마감 날짜는 필수입니다.")
    private LocalDateTime dueDate;

    @NotNull(message = "우선 순위는 필수입니다.")
    private TodoPriority priority;

    @NotNull(message = "일의 상태는 필수입니다.")
    private TodoStatus status;

    private TimeType timeType;

    @Builder
    private UpdateTodoRequest(final Long todoId, final String title,
                              final String description, final LocalDateTime startDate,
                              final LocalDateTime dueDate, final TodoPriority priority,
                              final TodoStatus status,final TimeType timeType
    ) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.timeType = timeType;
    }

}
