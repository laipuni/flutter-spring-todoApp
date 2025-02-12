package project.app.flutter_spring_todoapp.todo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "마감 날짜는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull(message = "우선 순위는 필수입니다.")
    private TodoPriority priority;

    @NotNull(message = "일의 상태는 필수입니다.")
    private TodoStatus status;

    @Builder
    private UpdateTodoRequest(final Long todoId, final String title,
                              final String description, final LocalDate startDate,
                              final LocalDate dueDate, final TodoPriority priority,
                              final TodoStatus status
    ) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

}
