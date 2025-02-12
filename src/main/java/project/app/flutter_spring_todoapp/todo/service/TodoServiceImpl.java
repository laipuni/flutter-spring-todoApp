package project.app.flutter_spring_todoapp.todo.service;

import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.AddTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.DetailTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoListResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.UpdateTodoResponse;

public class TodoServiceImpl implements TodoService{
    @Override
    public AddTodoResponse save(final AddTodoRequest request) {
        return null;
    }

    @Override
    public TodoListResponse findAll() {
        return null;
    }

    @Override
    public UpdateTodoResponse update(final UpdateTodoRequest request) {
        return null;
    }

    @Override
    public DetailTodoResponse detailTodo(final Long postId) {
        return null;
    }
}
