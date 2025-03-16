package project.app.flutter_spring_todoapp.todo.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import project.app.flutter_spring_todoapp.todo.domain.Todo;

import java.util.List;

@Getter
@NoArgsConstructor
public class TodoListResponse {

    private int size;
    private boolean hasNext;
    private List<TodoItemResponse> todoList;

    @Builder
    private TodoListResponse(final int size,final boolean hasNext, final List<TodoItemResponse> todoList) {
        this.size = size;
        this.hasNext = hasNext;
        this.todoList = todoList;
    }

    public static TodoListResponse of(final List<Todo> todoList){
        return TodoListResponse.builder()
                .size(todoList.size())
                .hasNext(false)
                .todoList(todoList.stream()
                        .map(TodoItemResponse::of)
                        .toList()
                )
                .build();
    }

    public static TodoListResponse of(final List<TodoItemResponse> todoList, boolean hasNext){
        return TodoListResponse.builder()
                .size(todoList.size())
                .hasNext(hasNext)
                .todoList(todoList)
                .build();
    }

}
