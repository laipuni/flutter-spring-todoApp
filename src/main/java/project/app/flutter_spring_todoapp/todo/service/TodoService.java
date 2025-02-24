package project.app.flutter_spring_todoapp.todo.service;

import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;
import project.app.flutter_spring_todoapp.todo.TodoUpdateDto;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.TodoDeleteDto;
import project.app.flutter_spring_todoapp.todo.dto.TodoSaveDto;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.*;

public interface TodoService {

    public Todo save(final TodoSaveDto todoServiceDto, final Long memberId);

    public TodoListResponse findAll(final Long memberId);

    public Todo update(final TodoUpdateDto updateDto);

    public DetailTodoResponse detailTodo(final Long todoId);

    public DeleteTodoResponse removeTodoBy(final TodoDeleteDto deleteDto);
}
