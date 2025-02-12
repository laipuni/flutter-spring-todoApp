package project.app.flutter_spring_todoapp.todo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class AddTodoRequest {

    @NotEmpty(message = "제목은 필수 입력칸 입니다.")
    @Size(max = 32,message = "최대 32자까지 작성할 수 있습니다.")
    private String title;

    private String description;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "마감 날짜는 필수입니다.")
    private LocalDate duetDate;

    @NotNull(message = "우선 순위는 필수입니다.")
    private TodoPriority priority;

    @Builder
    private AddTodoRequest(final String title, final String description, final LocalDate startDate, final LocalDate duetDate, final TodoPriority priority) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.duetDate = duetDate;
        this.priority = priority;
    }
}
