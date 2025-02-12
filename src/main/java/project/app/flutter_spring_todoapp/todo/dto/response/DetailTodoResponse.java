package project.app.flutter_spring_todoapp.todo.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DetailTodoResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private TodoStatus status;
    private TodoPriority priority;

    @Builder
    private DetailTodoResponse(final Long id, final String title, final String description,
                            final LocalDate startDate, final LocalDate dueDate,
                            final TodoStatus status, final TodoPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

}
