package project.app.flutter_spring_todoapp.todo.dto.response;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoItemResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private TodoStatus status;
    private TodoPriority priority;

    @Builder
    public TodoItemResponse(final Long id, final String title, final String description,
                            final LocalDateTime startDate, final LocalDateTime dueDate,
                            final TodoStatus status, final TodoPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    public static TodoItemResponse of(final Todo todo) {
        return TodoItemResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .startDate(todo.getStartDate())
                .dueDate(todo.getDueDate())
                .status(todo.getStatus())
                .priority(todo.getPriority())
                .build();
    }
}
