package project.app.flutter_spring_todoapp.todo.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class UpdateTodoResponse {

    private Long todoId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private TodoPriority priority;
    private TodoStatus status;

    @Builder
    private UpdateTodoResponse(final Long todoId, final String title,
                              final String description, final LocalDateTime startDate,
                              final LocalDateTime dueDate, final TodoPriority priority,
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

    public static UpdateTodoResponse of(final Todo todo){
        return UpdateTodoResponse.builder()
                .title(todo.getTitle())
                .description(todo.getDescription())
                .startDate(todo.getStartDate())
                .dueDate(todo.getDueDate())
                .status(todo.getStatus())
                .priority(todo.getPriority())
                .build();
    }
}

