package project.app.flutter_spring_todoapp.todo.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.flutter_spring_todoapp.todo.domain.Todo;

import java.util.List;

@Getter
@NoArgsConstructor
public class TodoListResponse {

    private int size;
    private List<TodoItemResponse> todoList;

    @Builder
    private TodoListResponse(final int size, final List<TodoItemResponse> todoList) {
        this.size = size;
        this.todoList = todoList;
    }

    public static TodoListResponse of(final List<Todo> todoList){
        return TodoListResponse.builder()
                .size(todoList.size())
                .todoList(todoList.stream()
                        .map(TodoItemResponse::of)
                        .toList()
                )
                .build();
    }

}
