package project.app.flutter_spring_todoapp.todo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoSaveDto {

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private TodoStatus status;
    private TodoPriority priority;

    public static TodoSaveDto of(final String title, final String description, final LocalDateTime startDate,
                                 final LocalDateTime dueDate, final TodoStatus status, final TodoPriority priority){
        return TodoSaveDto.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
    }

    public Todo toEntity(final Member member){
        return Todo.of(this.title,this.description,this.startDate,this.dueDate,this.status,this.priority,member);
    }
}
