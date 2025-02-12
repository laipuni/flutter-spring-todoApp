package project.app.flutter_spring_todoapp.todo.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class UpdateTodoResponse {

    private Long todoId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private TodoPriority priority;
    private TodoStatus status;

    @Builder
    private UpdateTodoResponse(final Long todoId, final String title,
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

