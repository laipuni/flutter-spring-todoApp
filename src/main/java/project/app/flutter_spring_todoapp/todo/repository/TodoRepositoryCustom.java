package project.app.flutter_spring_todoapp.todo.repository;

import project.app.flutter_spring_todoapp.todo.dto.response.TodoItemResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoListResponse;

import java.util.List;

public interface TodoRepositoryCustom {

    TodoListResponse findTodosByMemberWithFilters(final Long memberId, final int page, final String search, final String order, final String sort);

}
