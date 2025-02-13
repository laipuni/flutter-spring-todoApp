package project.app.flutter_spring_todoapp.todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteTodoRequest {

    @NotNull(message = "삭제할 할일의 id는 필수입니다.")
    private Long todoId;

    @Builder
    private DeleteTodoRequest(final Long todoId) {
        this.todoId = todoId;
    }


}
