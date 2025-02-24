package project.app.flutter_spring_todoapp.todo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoDeleteDto {

    private Long todoId;
    private Long deleterId;

    public static TodoDeleteDto of(final Long todoId, final Long deleterId){
        return TodoDeleteDto.builder()
                .todoId(todoId)
                .deleterId(deleterId)
                .build();
    }
}
