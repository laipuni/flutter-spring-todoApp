package project.app.flutter_spring_todoapp.todo.service;

import org.springframework.stereotype.Service;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.DeleteTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.*;

public interface TodoService {

    public AddTodoResponse save(final AddTodoRequest request);

    public TodoListResponse findAll();

    public UpdateTodoResponse update(final UpdateTodoRequest request);

    public DetailTodoResponse detailTodo(final Long todoId);

    public DeleteTodoResponse removeTodoBy(final Long todoId);

}
