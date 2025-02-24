package project.app.flutter_spring_todoapp.todo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class AddTodoRequest {

    @NotEmpty(message = "제목은 필수 입력칸 입니다.")
    @Size(max = 32,message = "최대 32자까지 작성할 수 있습니다.")
    private String title;

    private String description;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "마감 날짜는 필수입니다.")
    private LocalDateTime duetDate;

    @NotNull(message = "우선 순위는 필수입니다.")
    private TodoPriority priority;

    private TodoStatus status;

    private TimeType timeType = TimeType.NONE; //기본 값으로는 None 상태

    @Builder
    private AddTodoRequest(final String title, final String description,
                           final LocalDateTime startDate, final LocalDateTime duetDate,
                           final TodoStatus status, final TodoPriority priority, final  TimeType timeType) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.duetDate = duetDate;
        this.status = status;
        this.priority = priority;
        this.timeType = timeType;
    }

    public Todo toEntity(final Member member) {
        return Todo.of(this.title,this.description,this.startDate,
                this.duetDate,this.status, this.priority,member);
    }
}
