package project.app.flutter_spring_todoapp.todo.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteTodoResponse {

    private Long todoId;

    @Builder
    private DeleteTodoResponse(final Long todoId) {
        this.todoId = todoId;
    }

    public static DeleteTodoResponse of(final Long todoId) {
        return DeleteTodoResponse.builder()
                .todoId(todoId)
                .build();
    }
}
